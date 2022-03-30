package com.example.smartmobilefactory_app_jc.page.favorite



import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.favorite.Contract.Command
import com.example.smartmobilefactory_app_jc.page.favorite.Contract.State
import com.example.smartmobilefactory_app_jc.arch.ArchInteractor
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.page.detail.DetailNavigator
import com.example.smartmobilefactory_app_jc.usecase.FetchFavorites
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@FragmentScoped
class FavoriteInteractor @Inject constructor(
    private val fetchFavorites: FetchFavorites,
    private val detailNavigator: DetailNavigator,
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
        is Command.Initial -> initial()
        is Command.SeriesSelected -> seriesSelected(command.id)
    }

    private fun initial(): Observable<State> {
        return fetchFavorites.all()
            .map<State> { shows ->
                State.Favorites(
                    series = shows.sortedBy { it.name },
                )
            }
            .toObservable()
    }

    private fun seriesSelected(id: Long): Observable<State> {
        detailNavigator.openDetail(id)
        return Observable.empty()
    }

}
