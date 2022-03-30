package com.example.smartmobilefactory_app_jc.page.detail

import androidx.annotation.DrawableRes
import com.example.smartmobilefactory_app_jc.arch.ArchView
import com.example.smartmobilefactory_app_jc.data.Show

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        data class Initial(
            val id: Long?,
        ) : Command()

        object EpisodesClick : Command()

        object Favorite : Command()

        object UnFavorite : Command()

        object None : Command()

    }

    sealed class State {

        object InvalidIdState : State()

        data class ErrorState(
            val message: String,
        ) : State()

        data class DataState(
            val show: Show,
            val favorite: Boolean,
            val episodesStatus: EpisodesStatus,
        ) : State()

    }

    enum class EpisodesStatus {
        FETCHING, FETCHED, ERROR
    }

    sealed class Model {

        object Loading : Model()

        data class Error(
            val message: String,
        ) : Model()

        data class NoShow(
            val message: String,
        ) : Model()

        data class ShowModel(
            val show: Show,
            val favorite: Boolean,
            val summary: CharSequence,
            @DrawableRes val monday: Int,
            @DrawableRes val tuesday: Int,
            @DrawableRes val wednesday: Int,
            @DrawableRes val thursday: Int,
            @DrawableRes val friday: Int,
            @DrawableRes val saturday: Int,
            @DrawableRes val sunday: Int,
            val episodes: Episodes,
        ) : Model()

    }

    sealed class Episodes {

        object Loading : Episodes()

        data class Loaded(
            val callToAction: String,
        ) : Episodes()

    }

}
