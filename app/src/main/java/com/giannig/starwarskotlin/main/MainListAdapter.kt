package com.giannig.starwarskotlin.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.giannig.starwarskotlin.R
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto

/**
 * Adapter for The [RecyclerView] of [MainActivity]
 */
class MainListAdapter : RecyclerView.Adapter<MainListAdapter.MainListViewHolder>() {

    private var list: List<StarWarsSinglePlanetDto> = emptyList()
    private var onItemClick: (Int) -> Unit = {}

    /**
     * Add planets into the list
     */
    fun addValues(newList: List<StarWarsSinglePlanetDto>) {
        list = list.plus(newList)
        notifyDataSetChanged()
    }

    /**
     * set Click listener of the planet
     */
    fun setClickLister(onClick: (Int) -> Unit) {
        onItemClick = onClick
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val ctx = parent.context
        val inflate = LayoutInflater.from(ctx)
        val v = inflate.inflate(R.layout.main_list_item, parent, false)
        return MainListViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val item = list[position]
        holder.set(item){
            onItemClick(position)
        }
    }

    /**
     * Planet list View Holder
     */
    class MainListViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val planetNameText = v.findViewById<TextView>(R.id.textPlanetName)
        private val planetImage = v.findViewById<ImageView>(R.id.planetImage)
        private val terrainText = v.findViewById<TextView>(R.id.terrainText)

        /**
         * set the view of the planet item
         */
        fun set(item: StarWarsSinglePlanetDto, onClick: (View) -> Unit) {
            planetNameText.text = item.name
            terrainText.text = item.terrain

            v.setOnClickListener{onClick(it)}

            Glide
                .with(v)
                .load(item.url)
                .centerCrop()
                .placeholder(R.drawable.empire)
                .into(planetImage)

        }
    }
}