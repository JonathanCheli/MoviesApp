package com.example.smartmobilefactory_app_jc.page.favorite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.tile.SeriesTile
import com.example.smartmobilefactory_app_jc.tile.SeriesTileCallback

class FavoriteViewHolder(
    itemView: View,
    callback: SeriesTileCallback,
) : RecyclerView.ViewHolder(itemView) {

    private val seriesTile = SeriesTile(itemView, callback)

    fun bind(show: Show) {
        seriesTile.bind(show)
    }

}
