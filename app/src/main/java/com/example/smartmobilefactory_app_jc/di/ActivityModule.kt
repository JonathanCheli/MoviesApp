package com.example.smartmobilefactory_app_jc.di

import com.example.smartmobilefactory_app_jc.di.ActivityModule.Declarations
import com.example.smartmobilefactory_app_jc.page.detail.DetailActivity
import com.example.smartmobilefactory_app_jc.page.episode.EpisodeActivity
import com.example.smartmobilefactory_app_jc.page.seasons.SeasonsActivity
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.example.smartmobilefactory_app_jc.page.detail.Contract as DetailContract
import com.example.smartmobilefactory_app_jc.page.episode.Contract as EpisodeContract
import com.example.smartmobilefactory_app_jc.page.seasons.Contract as SeasonsContract

@Module(includes = [Declarations::class])
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun providesCompatActivity(impl: Activity) = impl as AppCompatActivity

    @[Module InstallIn(ActivityComponent::class)]
    interface Declarations {

        @Binds
        fun bindDetailView(impl: DetailActivity): DetailContract.View

        @Binds
        fun bindEpisodeView(impl: EpisodeActivity): EpisodeContract.View

        @Binds
        fun bindSeasonsView(impl: SeasonsActivity): SeasonsContract.View

    }

}