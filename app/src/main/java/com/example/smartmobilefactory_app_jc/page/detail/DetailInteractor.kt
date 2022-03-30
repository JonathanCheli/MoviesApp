package com.example.smartmobilefactory_app_jc.page.detail


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.detail.Contract.Command
import com.example.smartmobilefactory_app_jc.page.detail.Contract.State
import com.example.smartmobilefactory_app_jc.arch.ArchInteractor
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.ext.valueOrError
import com.example.smartmobilefactory_app_jc.page.seasons.SeasonsNavigator
import com.example.smartmobilefactory_app_jc.usecase.*
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class DetailInteractor @Inject constructor(
    private val fetchShow: FetchShow,
    private val fetchSeasons: FetchSeasons,
    private val fetchEpisodes: FetchEpisodes,
    private val favoriteCheck: FavoriteCheck,
    private val favoriteToggler: FavoriteToggler,
    private val seasonsNavigator: SeasonsNavigator,
    private val subscriptions: CompositeDisposable,
    @QualifiedScheduler(COMPUTATION) private val computationScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) : ArchInteractor<Command, State>(
    errorFactory = State::ErrorState,
    subscriptions = subscriptions,
    backgroundScheduler = computationScheduler,
    foregroundScheduler = mainThreadScheduler,
    output = BehaviorSubject.create(),
) {

    override fun map(
        command: Command,
    ): Observable<State> = when (command) {
        is Command.Initial -> initial(command.id)
        is Command.EpisodesClick -> episodesClick()
        is Command.None -> Observable.empty()
        is Command.Favorite -> favorite()
        is Command.UnFavorite -> unFavorite()
    }

    private fun initial(id: Long?): Observable<State> {
        if (id == null) return Observable.just(State.InvalidIdState)
        return fetchShow.byId(id)
            .flatMap<State> { show ->
                Single.zip(
                    fetchSeasons.isCached(show),
                    favoriteCheck.isFavorite(show.id),
                    { cached, favorite ->
                        Pair(cached, favorite)
                    }
                ).map { (cached, favorite) ->
                    fetchSeasons(show)
                    State.DataState(
                        show = show,
                        favorite = favorite,
                        episodesStatus = if (cached) Contract.EpisodesStatus.FETCHED else Contract.EpisodesStatus.FETCHING,
                    )
                }
            }
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

    private fun episodesClick(): Observable<State> {
        return output.valueOrError()
            .map { state ->
                if (state is State.DataState) {
                    if (state.episodesStatus == Contract.EpisodesStatus.FETCHED) {
                        seasonsNavigator.openSeason(state.show.id)
                        state
                    } else {
                        fetchSeasons(state.show)
                        state.copy(episodesStatus = Contract.EpisodesStatus.FETCHING)
                    }
                } else {
                    state
                }
            }
            .toObservable()
    }

    private fun favorite(): Observable<State> {
        return output.valueOrError()
            .toObservable()
            .map { state ->
                if (state is State.DataState) {
                    subscriptions.add(
                        favoriteToggler.favorite(state.show.id)
                            .subscribe({}, Timber::e)
                    )
                    state.copy(favorite = true)
                } else {
                    state
                }
            }
            .onErrorResumeNext(::mapError)
    }

    private fun unFavorite(): Observable<State> {
        return output.valueOrError()
            .toObservable()
            .map { state ->
                if (state is State.DataState) {
                    subscriptions.add(
                        favoriteToggler.unfavorite(state.show.id)
                            .subscribe({}, Timber::e)
                    )
                    state.copy(favorite = false)
                } else {
                    state
                }
            }
            .onErrorResumeNext(::mapError)
    }

    private fun fetchSeasons(show: Show) {
        subscriptions.add(
            fetchSeasons.newest(show)
                .flattenAsObservable { it }
                .flatMap { season ->
                    fetchEpisodes.newest(season)
                        .flattenAsObservable { it }
                }
                .subscribe({}, {
                    fetchSeasonsDone(false)
                }, {
                    fetchSeasonsDone(true)
                })
        )
    }

    private fun fetchSeasonsDone(success: Boolean) {
        subscriptions.add(
            output.valueOrError()
                .subscribe({ state ->
                    if (state is State.DataState) {
                        output.onNext(
                            state.copy(
                                episodesStatus = if (success) Contract.EpisodesStatus.FETCHED else Contract.EpisodesStatus.ERROR,
                            )
                        )
                    }
                }, Timber::e)
        )
    }

}
