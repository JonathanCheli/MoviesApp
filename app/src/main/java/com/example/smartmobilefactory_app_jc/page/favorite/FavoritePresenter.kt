package com.example.smartmobilefactory_app_jc.page.favorite

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.favorite.Contract.Model
import com.example.smartmobilefactory_app_jc.page.favorite.Contract.State
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
class FavoritePresenter @Inject constructor(
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
            is State.NoFavorites -> Model.Empty(
                context.getString(R.string.favorite_empty),
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.Favorites -> {
                if (state.series.isEmpty()) {
                    Model.Empty(
                        context.getString(R.string.favorite_empty),
                    )
                } else {
                    Model.DataModel(state.series)
                }
            }
        }
    }

}
