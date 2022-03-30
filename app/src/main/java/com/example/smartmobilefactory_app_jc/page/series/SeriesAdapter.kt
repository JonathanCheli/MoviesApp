package com.example.smartmobilefactory_app_jc.page.series

import com.example.smartmobilefactory_app_jc.page.series.SeriesViewHolder.LoadViewHolder
import com.example.smartmobilefactory_app_jc.page.series.SeriesViewHolder.SeriesItemViewHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobilefactory_app_jc.R
import com.example.smartmobilefactory_app_jc.data.Show
import com.example.smartmobilefactory_app_jc.tile.SeriesTileCallback

class SeriesAdapter(
    private val loadReachCallback: () -> Unit,
    private val callback: SeriesTileCallback,
) : RecyclerView.Adapter<SeriesViewHolder>() {

    private val viewTypeItem = 1
    private val viewTypeLoad = 2
    private val data = mutableListOf<Show>()
    private var addNextLoad = false

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= data.size) viewTypeLoad else viewTypeItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == viewTypeItem) {
            SeriesItemViewHolder(inflater.inflate(R.layout.item_series, parent, false), callback)
        } else {
            LoadViewHolder(inflater.inflate(R.layout.item_load, parent, false))
        }
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        if (holder is SeriesItemViewHolder) {
            holder.bind(data[position])
        } else {
            loadReachCallback()
        }
    }

    override fun getItemCount(): Int = data.size + if (addNextLoad) 1 else 0

    override fun getItemId(position: Int): Long = if (position >= data.size) -1 else data[position].id

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Show>, addNextLoad: Boolean) {
        data.clear()
        data.addAll(items)
        this.addNextLoad = addNextLoad
        notifyDataSetChanged()
    }

}
