package com.example.smartmobilefactory_app_jc.usecase

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Season
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.network.api.TvShowService
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchSeasons @Inject constructor(
    private val service: TvShowService,
    private val db: AppDatabase,
    private val mapSeasons: MapSeasons,
    private val saveSeasons: SaveSeasons,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun isCached(show: Show): Single<Boolean> = db.seasonDao()
        .count(show.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { it > 0 }

    fun cached(show: Show): Single<List<Season>> = db.seasonDao()
        .getAllByShowId(show.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(mapSeasons::mapSeason)
        }

    fun newest(show: Show): Single<List<Season>> = service.seasons(show.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(mapSeasons::mapSeason)
        }
        .doOnSuccess { seasons ->
            saveSeasons.save(seasons, show)
        }

}
