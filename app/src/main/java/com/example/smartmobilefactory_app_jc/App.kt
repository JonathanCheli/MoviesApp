package com.example.smartmobilefactory_app_jc

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import com.example.smartmobilefactory_app_jc.arch.ArchBinder
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    // creates arch binder before any activity
    @Inject lateinit var archBinder: ArchBinder

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}