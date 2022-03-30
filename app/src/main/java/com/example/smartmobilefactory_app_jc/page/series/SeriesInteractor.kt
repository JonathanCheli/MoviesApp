package com.example.smartmobilefactory_app_jc.page.series

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.series.Contract.Command
import com.example.smartmobilefactory_app_jc.page.series.Contract.State
import com.example.smartmobilefactory_app_jc.arch.ArchInteractor
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.ext.valueOrError
import com.example.smartmobilefactory_app_jc.page.detail.DetailNavigator
import com.example.smartmobilefactory_app_jc.usecase.FetchShows
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@FragmentScoped
class SeriesInteractor @Inject constructor(
    private val fetchShows: FetchShows,
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
        is Command.NextPage -> nextPage()
        is Command.ChangeSort -> changeSort()
        is Command.SeriesSelected -> seriesSelected(command.id)
    }

    private var currentPage = 0
        @Synchronized get
        @Synchronized set

    private var waitingNextPage = false
        @Synchronized get
        @Synchronized set

    private fun initial(): Observable<State> {
        currentPage = 0
        waitingNextPage = true
        return fetchShows.cached()
            .map { shows ->
                State.DataState(
                    series = shows,
                    hasNextPage = false,
                    sortOption = Contract.SortOption.ID,
                )
            }
            .map(::sortedShows)
            .doOnSuccess { state ->
                if (state is State.DataState && state.series.isNotEmpty()) {
                    output.onNext(state)
                }
            }
            .flatMapObservable { state ->
                var observer = fetchShows.firstPage()
                    .flatMapObservable<State> { shows ->
                        if (state is State.DataState) {
                            BehaviorSubject.just(
                                state.copy(
                                    series = mergeSeries(shows, state),
                                    hasNextPage = true,
                                )
                            )
                        } else {
                            BehaviorSubject.just(
                                State.DataState(
                                    series = shows,
                                    hasNextPage = true,
                                    sortOption = Contract.SortOption.ID,
                                )
                            )
                        }
                    }
                    .map(::sortedShows)
                observer = if (state is State.DataState && state.series.isNotEmpty()) {
                    observer.onErrorReturn { state.copy(hasNextPage = false) }
                } else {
                    observer.onErrorResumeNext(::mapError)
                }
                observer
            }
            .doOnNext {
                waitingNextPage = false
            }
    }

    private fun nextPage(): Observable<State> {
        if (waitingNextPage) return Observable.empty()

        waitingNextPage = true
        currentPage++

        return fetchShows.numberedPage(currentPage)
            .flatMapObservable { shows ->
                output.valueOrError()
                    .flatMapObservable<State> { lastState ->
                        if (lastState is State.DataState) {
                            BehaviorSubject.just(
                                lastState.copy(
                                    series = mergeSeries(shows, lastState),
                                )
                            )
                        } else {
                            BehaviorSubject.just(
                                State.DataState(
                                    series = shows,
                                    hasNextPage = true,
                                    sortOption = Contract.SortOption.ID,
                                )
                            )
                        }
                    }
                    .onErrorResumeNext(::mapError)
            }
            .onErrorResumeNext { error ->
                output.valueOrError()
                    .flatMapObservable { lastState ->
                        if (lastState is State.DataState) {
                            BehaviorSubject.just(
                                lastState.copy(
                                    hasNextPage = false,
                                )
                            )
                        } else {
                            mapError(error)
                        }
                    }
                    .onErrorResumeNext(::mapError)
            }
            .map(::sortedShows)
            .doOnNext {
                waitingNextPage = false
            }
    }

    private fun changeSort(): Observable<State> {
        return output.valueOrError()
            .map { state ->
                if (state is State.DataState) {
                    state.copy(
                        sortOption = if (state.sortOption == Contract.SortOption.ID) {
                            Contract.SortOption.NAME
                        } else {
                            Contract.SortOption.ID
                        }
                    )
                } else {
                    state
                }
            }
            .map(::sortedShows)
            .toObservable()
    }

    private fun seriesSelected(id: Long): Observable<State> {
        detailNavigator.openDetail(id)
        return Observable.empty()
    }

    private fun mergeSeries(base: List<Show>, state: State): List<Show> {
        return if (state is State.DataState) {
            val mergedShows = base.toMutableList()
            val ids = base.map { it.id }.toSet()
            mergedShows.addAll(state.series.filter { !ids.contains(it.id) })
            mergedShows
        } else {
            base
        }
    }

    private fun sortedShows(state: State): State {
        return if (state is State.DataState) {
            val sortedList = when (state.sortOption) {
                Contract.SortOption.ID -> state.series.sortedBy { it.id }
                Contract.SortOption.NAME -> state.series.sortedBy { it.name }
            }
            state.copy(
                series = sortedList,
            )
        } else {
            state
        }
    }

}
