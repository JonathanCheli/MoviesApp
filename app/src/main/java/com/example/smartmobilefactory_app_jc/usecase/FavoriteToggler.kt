package com.example.smartmobilefactory_app_jc.usecase

import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.db.AppDatabase
import com.example.smartmobilefactory_app_jc.db.FavoriteEntity

import dagger.Reusable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler


import javax.inject.Inject

@Reusable
class FavoriteToggler @Inject constructor(
    private val db: AppDatabase,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun favorite(showId: Long): Completable = db.favoriteDao()
        .insert(FavoriteEntity(showId))
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)

    fun unfavorite(showId: Long): Completable = db.favoriteDao()
        .delete(showId)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)

}