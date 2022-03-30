package com.example.smartmobilefactory_app_jc.usecase



import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.data.Season
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveSeasons @Inject constructor(
    private val db: AppDatabase,
    private val mapSeasons: MapSeasons,
    private val subscriptions: CompositeDisposable,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
) {

    fun save(seasons: List<Season>, show: Show) {
        seasons.map { mapSeasons.mapSeason(it, show) }.onEach { season ->
            db.seasonDao().let { dao ->
                subscriptions.add(
                    dao.insert(season)
                        .subscribeOn(ioScheduler)
                        .subscribe({}, Timber::e)
                )
            }
        }
    }

}
