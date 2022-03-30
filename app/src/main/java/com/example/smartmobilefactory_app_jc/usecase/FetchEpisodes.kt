package com.example.smartmobilefactory_app_jc.usecase

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Episode
import com.example.smartmobilefactory_app_jc.data.Season
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.network.api.TvShowService
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchEpisodes @Inject constructor(
    private val service: TvShowService,
    private val db: AppDatabase,
    private val mapEpisodes: MapEpisodes,
    private val saveEpisodes: SaveEpisodes,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(season: Season): Single<List<Episode>> = db.episodeDao()
        .getAllBySeasonId(season.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(mapEpisodes::mapEpisode)
        }

    fun newest(season: Season): Single<List<Episode>> = service.episodes(season.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(mapEpisodes::mapEpisode)
        }
        .doOnSuccess { episodes ->
            saveEpisodes.save(episodes, season)
        }

}
