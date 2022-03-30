package com.example.smartmobilefactory_app_jc.page.seasons

import com.example.smartmobilefactory_app_jc.arch.ArchView
import com.example.smartmobilefactory_app_jc.data.Episode
import com.example.smartmobilefactory_app_jc.data.Season
import com.example.smartmobilefactory_app_jc.data.Show

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        data class Initial(
            val id: Long?,
        ) : Command()

        data class EpisodeClick(
            val id: Long,
        ) : Command()

        object None : Command()

    }

    sealed class State {

        object InvalidIdState : State()

        data class ErrorState(
            val message: String,
        ) : State()

        data class DataState(
            val show: Show,
            val seasons: Map<Season, Set<Episode>>,
        ) : State()

    }

    sealed class Model {

        object Loading : Model()

        data class Error(
            val message: String,
        ) : Model()

        data class DataModel(
            val title: String,
            val items: List<Item>,
        ) : Model()

    }

    sealed class Item {

        data class SeasonHeader(
            val title: String,
        ) : Item()

        data class Episode(
            val id: Long,
            val title: String,
        ) : Item()

    }

}
