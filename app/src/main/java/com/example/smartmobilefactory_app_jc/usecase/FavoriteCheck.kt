package com.example.smartmobilefactory_app_jc.usecase

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import dagger.Reusable

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FavoriteCheck @Inject constructor(
    private val db: AppDatabase,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun isFavorite(showId: Long): Single<Boolean> = db.favoriteDao()
        .count(showId)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { it > 0 }

}
