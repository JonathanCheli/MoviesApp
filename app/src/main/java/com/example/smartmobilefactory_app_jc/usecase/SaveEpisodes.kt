package com.example.smartmobilefactory_app_jc.usecase


import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Episode
import com.example.smartmobilefactory_app_jc.data.Season
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveEpisodes @Inject constructor(
    private val db: AppDatabase,
    private val mapEpisodes: MapEpisodes,
    private val subscriptions: CompositeDisposable,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
) {

    fun save(episodes: List<Episode>, season: Season) {
        episodes.map { mapEpisodes.mapEpisode(it, season) }.onEach { episode ->
            db.episodeDao().let { dao ->
                subscriptions.add(
                    dao.insert(episode)
                        .subscribeOn(ioScheduler)
                        .subscribe({}, Timber::e)
                )
            }
        }
    }

}
