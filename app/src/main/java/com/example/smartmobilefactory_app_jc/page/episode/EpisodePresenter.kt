package com.example.smartmobilefactory_app_jc.page.episode


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.episode.Contract.Model
import com.example.smartmobilefactory_app_jc.page.episode.Contract.State
import android.content.Context
import androidx.core.text.HtmlCompat
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.arch.ArchPresenter
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@ActivityScoped
class EpisodePresenter @Inject constructor(
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
            is State.InvalidIdState -> Model.NoShow(
                message = context.getString(R.string.episode_no_data),
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.DataState -> {
                Model.EpisodeModel(
                    episode = state.episode,
                    summary = HtmlCompat.fromHtml(state.episode.summary,
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    ),
                )
            }
        }
    }

}
