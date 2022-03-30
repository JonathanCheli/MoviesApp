package com.example.smartmobilefactory_app_jc.di



import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.IO
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import com.example.smartmobilefactory_app_jc.di.qualifiers.QualifiedScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object RxModule {

    @Provides
    fun providesCompositeDisposable() = CompositeDisposable()

    @[Provides Singleton QualifiedScheduler(IO)]
    fun providesSchedulerIo(): Scheduler = Schedulers.io()

    @[Provides Singleton QualifiedScheduler(COMPUTATION)]
    fun providesSchedulerComputation(): Scheduler = Schedulers.computation()

    @[Provides Singleton QualifiedScheduler(MAIN_THREAD)]
    fun providesSchedulerMainThread(): Scheduler = AndroidSchedulers.mainThread()

}
