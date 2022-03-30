package com.example.smartmobilefactory_app_jc.page.search


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.search.Contract.Model
import com.example.smartmobilefactory_app_jc.page.search.Contract.State
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
class SearchPresenter @Inject constructor(
    @ApplicationContext private val context: Context,
    subscriptions: CompositeDisposable,
    @QualifiedScheduler(COMPUTATION) private val computationScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) : ArchPresenter<State, Model>(
    errorFactory = Model::Error,
    subscriptions = subscriptions,
    backgroundScheduler = computationScheduler,
    foregroundScheduler = mainThreadScheduler,
    output = BehaviorSubject.createDefault(Model.Loading),
) {

    override fun map(state: State): Model {
        return when (state) {
            is State.NoSearch -> Model.Empty(
                context.getString(R.string.search_empty),
            )
            is State.Searching -> Model.Loading
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.SearchResult -> {
                if (state.series.isEmpty()) {
                    Model.Empty(
                        context.getString(R.string.search_not_found),
                    )
                } else {
                    Model.DataModel(state.series)
                }
            }
        }
    }

}
