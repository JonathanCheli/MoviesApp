package com.example.smartmobilefactory_app_jc.page.detail

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.data.Genre

class GenreViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    fun bind(genre: Genre) {
        itemView.findViewById<TextView>(R.id.title).setText(genre.title)
    }

}
