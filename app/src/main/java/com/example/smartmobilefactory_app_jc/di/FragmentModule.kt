package com.example.smartmobilefactory_app_jc.di

import com.example.smartmobilefactory_app_jc.di.FragmentModule.Declarations
import com.example.smartmobilefactory_app_jc.page.favorite.FavoriteFragment
import com.example.smartmobilefactory_app_jc.page.search.SearchFragment
import com.example.smartmobilefactory_app_jc.page.series.SeriesFragment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import com.example.smartmobilefactory_app_jc.page.favorite.Contract as FavoriteContract
import com.example.smartmobilefactory_app_jc.page.search.Contract as SearchContract
import com.example.smartmobilefactory_app_jc.page.series.Contract as SeriesContract

@Module(includes = [Declarations::class])
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @[Module InstallIn(FragmentComponent::class)]
    interface Declarations {

        @Binds fun bindFavoriteView(impl: FavoriteFragment): FavoriteContract.View

        @Binds fun bindSearchView(impl: SearchFragment): SearchContract.View

        @Binds fun bindSeriesView(impl: SeriesFragment): SeriesContract.View

    }

}

