package com.example.smartmobilefactory_app_jc.page.search

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.search.Contract.Command
import com.example.smartmobilefactory_app_jc.page.search.Contract.State
import com.example.smartmobilefactory_app_jc.arch.ArchInteractor
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.page.detail.DetailNavigator
import com.example.smartmobilefactory_app_jc.usecase.SearchShows
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@FragmentScoped
class SearchInteractor @Inject constructor(
    private val searchShows: SearchShows,
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
        is Command.Search -> search(command.query)
        is Command.SeriesSelected -> seriesSelected(command.id)
    }

    private fun initial(): Observable<State> {
        return Observable.just(State.NoSearch)
    }

    private fun search(
        query: String,
    ): Observable<State> {
        if (query.isEmpty()) return Observable.just(State.NoSearch)

        output.onNext(State.Searching)
        return searchShows.search(query)
            .map<State> { shows ->
                State.SearchResult(
                    series = shows,
                )
            }
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

    private fun seriesSelected(id: Long): Observable<State> {
        detailNavigator.openDetail(id)
        return Observable.empty()
    }

}
