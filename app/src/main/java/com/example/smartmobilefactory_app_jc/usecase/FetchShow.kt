package com.example.smartmobilefactory_app_jc.usecase

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchShow @Inject constructor(
    private val db: AppDatabase,
    private val mapShows: MapShows,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun byId(id: Long): Single<Show> = db.showDao().getById(id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map(mapShows::mapShow)

}