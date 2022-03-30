package com.example.smartmobilefactory_app_jc.page.series

import com.example.smartmobilefactory_app_jc.page.series.Contract.State
import com.example.smartmobilefactory_app_jc.page.series.Contract.Model
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import android.content.Context
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.arch.ArchPresenter
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@FragmentScoped
class SeriesPresenter @Inject constructor(
    @ApplicationContext private val context: Context,
    subscriptions: CompositeDisposable,
    @QualifiedScheduler(COMPUTATION) private val computationScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) : ArchPresenter<State, Model>(
    errorFactory = { Model.Error(context.getString(R.string.page_title_series), it) },
    subscriptions = subscriptions,
    backgroundScheduler = computationScheduler,
    foregroundScheduler = mainThreadScheduler,
    output = BehaviorSubject.create(),
) {

    override fun map(state: State): Model {
        return when (state) {
            is State.DataState -> Model.DataModel(
                title = context.getString(R.string.page_title_series_enumerated, state.series.size),
                series = state.series,
                showNextLoad = state.hasNextPage,
            )
            is State.ErrorState -> Model.Error(
                title = context.getString(R.string.page_title_series),
                state.message,
            )
        }
    }

}
