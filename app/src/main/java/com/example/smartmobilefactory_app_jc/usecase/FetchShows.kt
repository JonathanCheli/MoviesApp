package com.example.smartmobilefactory_app_jc.usecase


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.network.api.TvShowService
import com.example.smartmobilefactory_app_jc.network.payload.ShowResponse
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchShows @Inject constructor(
    private val service: TvShowService,
    private val db: AppDatabase,
    private val mapShows: MapShows,
    private val saveShows: SaveShows,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(): Single<List<Show>> = db.showDao().getAll()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(mapShows::mapShow)
        }

    fun firstPage(): Single<List<Show>> = fetch(service.shows())

    fun numberedPage(page: Int): Single<List<Show>> = fetch(service.shows(page))

    private fun fetch(source: Single<List<ShowResponse>>): Single<List<Show>> = source
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(mapShows::mapShow)
        }
        .doOnSuccess(saveShows::save)

}