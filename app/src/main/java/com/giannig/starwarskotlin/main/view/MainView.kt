package com.giannig.starwarskotlin.main.view

import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto

interface MainView {
    fun loadView()
    fun updateList(list: List<StarWarsSinglePlanetDto>)
    fun showItemList()
    fun showErrorMessage(message: String?)
}