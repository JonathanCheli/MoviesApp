package com.example.smartmobilefactory_app_jc.page.seasons


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.seasons.Contract.State
import com.example.smartmobilefactory_app_jc.page.seasons.Contract.Command
import com.example.smartmobilefactory_app_jc.arch.ArchInteractor
import com.example.smartmobilefactory_app_jc.data.Episode
import com.example.smartmobilefactory_app_jc.data.Season
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.page.episode.EpisodeNavigator
import com.example.smartmobilefactory_app_jc.usecase.FetchEpisodes
import com.example.smartmobilefactory_app_jc.usecase.FetchSeasons
import com.example.smartmobilefactory_app_jc.usecase.FetchShow
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject


@ActivityScoped
class SeasonsInteractor @Inject constructor(
    private val fetchShow: FetchShow,
    private val fetchSeasons: FetchSeasons,
    private val fetchEpisodes: FetchEpisodes,
    private val episodeNavigator: EpisodeNavigator,
    subscriptions: CompositeDisposable,
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
        is Command.EpisodeClick -> episodesClick(command.id)
        is Command.None -> Observable.empty()
    }

    private fun initial(id: Long?): Observable<State> {
        if (id == null) return Observable.just(State.InvalidIdState)
        return fetchShow.byId(id)
            .flatMap { show ->
                fetchSeasons.cached(show)
                    .flattenAsObservable { it }
                    .flatMapSingle { season ->
                        fetchEpisodes.cached(season)
                            .map { it.toSet() }
                            .map { episodes ->
                                season to episodes
                            }
                    }
                    .collectInto(mutableMapOf<Season, Set<Episode>>()) { map, pair ->
                        map[pair.first] = pair.second
                    }
                    .map<State> { seasons ->
                        State.DataState(
                            show = show,
                            seasons = seasons,
                        )
                    }
            }
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

    private fun episodesClick(episodeId: Long): Observable<State> {
        episodeNavigator.openEpisode(episodeId)
        return Observable.empty()
    }

}
