package com.giannig.starwarskotlin.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.giannig.starwarskotlin.R
import com.giannig.starwarskotlin.model.dto.StarWarsPlanet

class MainListAdapter(private val onItemClick: (View) -> Unit) : RecyclerView.Adapter<MainListAdapter.MainListViewHolder>(){

   private var list:List<StarWarsPlanet> = emptyList()

    fun addValues(newList: List<StarWarsPlanet>) {
        list = list.plus(newList)
        notifyDataSetChanged()
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
        holder.set(item, onItemClick)
    }

    class MainListViewHolder (private val v : View): RecyclerView.ViewHolder(v) {
        private val planetNameText = v.findViewById<TextView>(R.id.textPlanetName)

        fun set(item: StarWarsPlanet, onClick: (View) -> Unit) {
            planetNameText.text = item.name
            v.setOnClickListener{onClick(it)}
        }
    }
}