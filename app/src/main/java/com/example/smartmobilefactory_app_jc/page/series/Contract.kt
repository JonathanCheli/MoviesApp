package com.example.smartmobilefactory_app_jc.page.series

import com.example.smartmobilefactory_app_jc.arch.ArchView
import com.example.smartmobilefactory_app_jc.data.Show

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        object Initial : Command()

        object NextPage : Command()

        object ChangeSort : Command()

        data class SeriesSelected(
            val id: Long,
        ) : Command()

    }

    sealed class State {

        data class DataState(
            val series: List<Show>,
            val hasNextPage: Boolean,
            val sortOption: SortOption,
        ) : State()

        data class ErrorState(
            val message: String,
        ) : State()

    }

    sealed class Model {

        object LoadModel : Model()

        data class DataModel(
            val title: String,
            val series: List<Show>,
            val showNextLoad: Boolean,
        ) : Model()

        data class Error(
            val title: String,
            val message: String,
        ) : Model()

    }

    enum class SortOption {
        ID,
        NAME,
    }

}