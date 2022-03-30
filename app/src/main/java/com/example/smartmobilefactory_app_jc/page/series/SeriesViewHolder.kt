package com.example.smartmobilefactory_app_jc.page.series

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.tile.SeriesTile
import com.example.smartmobilefactory_app_jc.tile.SeriesTileCallback

sealed class SeriesViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    class LoadViewHolder(
        itemView: View,
    ) : SeriesViewHolder(itemView)

    class SeriesItemViewHolder(
        itemView: View,
        callback: SeriesTileCallback,
    ) : SeriesViewHolder(itemView) {

        private val seriesTile = SeriesTile(itemView, callback)

        fun bind(show: Show) {
            seriesTile.bind(show)
        }

    }

}
