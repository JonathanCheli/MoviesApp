package com.example.smartmobilefactory_app_jc.page.detail

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.page.detail.Contract.Model
import com.example.smartmobilefactory_app_jc.page.detail.Contract.State
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.text.HtmlCompat
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.arch.ArchPresenter
import com.example.smartmobilefactory_app_jc.data.ScheduleDay
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@ActivityScoped
class DetailPresenter @Inject constructor(
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
                message = context.getString(R.string.detail_no_show),
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.DataState -> {
                Model.ShowModel(
                    show = state.show,
                    favorite = state.favorite,
                    summary = HtmlCompat.fromHtml(state.show.summary,
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    ),
                    monday = dayIcon(ScheduleDay.MONDAY, state.show),
                    tuesday = dayIcon(ScheduleDay.TUESDAY, state.show),
                    wednesday = dayIcon(ScheduleDay.WEDNESDAY, state.show),
                    thursday = dayIcon(ScheduleDay.THURSDAY, state.show),
                    friday = dayIcon(ScheduleDay.FRIDAY, state.show),
                    saturday = dayIcon(ScheduleDay.SATURDAY, state.show),
                    sunday = dayIcon(ScheduleDay.SUNDAY, state.show),
                    episodes = when (state.episodesStatus) {
                        Contract.EpisodesStatus.FETCHING -> Contract.Episodes.Loading
                        Contract.EpisodesStatus.FETCHED -> Contract.Episodes.Loaded(
                            context.getString(
                                R.string.detail_episodes_button
                            )
                        )
                        Contract.EpisodesStatus.ERROR -> Contract.Episodes.Loaded(
                            context.getString(
                                R.string.detail_episodes_button_retry
                            )
                        )
                    },
                )
            }
        }
    }

    @DrawableRes
    private fun dayIcon(scheduleDay: ScheduleDay, show: Show): Int {
        return if (scheduleDay in show.days) {
            R.drawable.bg_tile_schedule_day_on
        } else {
            R.drawable.bg_tile_schedule_day_off
        }
    }

}
