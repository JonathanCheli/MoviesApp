package com.example.smartmobilefactory_app_jc.page.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.tile.SeriesTileCallback

class FavoriteAdapter(
    private val callback: SeriesTileCallback,
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    private val data = mutableListOf<Show>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(inflater.inflate(R.layout.item_series, parent, false), callback)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].id

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Show>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

}
