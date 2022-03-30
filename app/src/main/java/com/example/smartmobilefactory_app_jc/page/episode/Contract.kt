package com.example.smartmobilefactory_app_jc.page.episode

import com.example.smartmobilefactory_app_jc.arch.ArchView
import com.example.smartmobilefactory_app_jc.data.Episode

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        data class Initial(
            val id: Long?,
        ) : Command()

    }

    sealed class State {

        object InvalidIdState : State()

        data class ErrorState(
            val message: String,
        ) : State()

        data class DataState(
            val episode: Episode,
        ) : State()

    }

    sealed class Model {

        object Loading : Model()

        data class Error(
            val message: String,
        ) : Model()

        data class NoShow(
            val message: String,
        ) : Model()

        data class EpisodeModel(
            val episode: Episode,
            val summary: CharSequence,
        ) : Model()

    }

}
