package com.example.smartmobilefactory_app_jc.page.seasons

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.R

sealed class SeasonsViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    class SeasonHeaderViewHolder(
        itemView: View,
    ) : SeasonsViewHolder(itemView) {

        fun bind(season: Contract.Item.SeasonHeader) {
            itemView.findViewById<TextView>(R.id.title).text = season.title
        }

    }

    class EpisodeItemViewHolder(
        itemView: View,
        callback: SeasonsEpisodeCallback,
    ) : SeasonsViewHolder(itemView) {

        private var lastId: Long? = null

        init {
            itemView.findViewById<View>(R.id.container).setOnClickListener {
                lastId?.let(callback::onEpisodeClick)
            }
        }

        fun bind(episode: Contract.Item.Episode) {
            lastId = episode.id
            itemView.findViewById<TextView>(R.id.title).text = episode.title
        }

    }

}
