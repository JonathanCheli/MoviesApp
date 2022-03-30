package com.example.smartmobilefactory_app_jc.page.episode


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.episode.Contract.Command
import com.example.smartmobilefactory_app_jc.page.episode.Contract.State
import com.example.smartmobilefactory_app_jc.arch.ArchInteractor
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.usecase.FetchEpisode
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@ActivityScoped
class EpisodeInteractor @Inject constructor(
    private val fetchEpisode: FetchEpisode,
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
    }

    private fun initial(id: Long?): Observable<State> {
        if (id == null) return Observable.just(State.InvalidIdState)
        return fetchEpisode.cached(id)
            .map<State>(State::DataState)
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

}
