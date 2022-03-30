package com.example.smartmobilefactory_app_jc.tile

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.data.Genre
import com.example.smartmobilefactory_app_jc.data.Show

class SeriesTile(
    private val itemView: View,
    private val callback: SeriesTileCallback,
) {

    private var currentId: Long? = null

    init {
        itemView.findViewById<View>(R.id.root).setOnClickListener {
            currentId?.let(callback::onItemClick)
        }
    }

    fun bind(show: Show) {
        currentId = show.id

        val name = itemView.findViewById<TextView>(R.id.name)
        val ratingValue = itemView.findViewById<TextView>(R.id.rating_value)
        val banner = itemView.findViewById<ImageView>(R.id.banner)
        val language = itemView.findViewById<ImageView>(R.id.language)
        name.text = show.name
        ratingValue.text = show.rating.toString()
        language.setImageResource(show.language.icon)
        language.contentDescription = itemView.context.getString(show.language.title)

        Glide.with(itemView)
            .load(show.image)
            .centerCrop()
            .placeholder(R.drawable.bg_name_contrast)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(banner)
    }

    private fun applyGenre(genres: Set<Genre>, view: ImageView, index: Int) {
        if (genres.size > index) {
            val genre = genres.toList()[index]
            view.setImageResource(genre.icon)
            view.contentDescription = itemView.context.getString(genre.title)
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

}

interface SeriesTileCallback {

    fun onItemClick(id: Long)

}
