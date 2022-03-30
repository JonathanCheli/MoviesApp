package com.example.smartmobilefactory_app_jc.usecase


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.network.api.TvShowService
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class SearchShows @Inject constructor(
    private val service: TvShowService,
    private val db: AppDatabase,
    private val mapShows: MapShows,
    private val saveShows: SaveShows,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun search(query: String): Single<List<Show>> = service.search(query)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull { it.show }.mapNotNull(mapShows::mapShow)
        }
        .doOnSuccess(saveShows::save)
        .onErrorResumeNext {
            // failed to search by api, try the best by search on local database
            db.showDao().search(query)
                .subscribeOn(ioScheduler)
                .observeOn(mainThreadScheduler)
                .map { entity ->
                    entity.map(mapShows::mapShow)
                }
        }

}