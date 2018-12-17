package com.giannig.starwarskotlin.details.view

import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanet

interface DetailsView {
    fun loading()
    fun showErrorMessage(message: String?)
    fun showData(planet: StarWarsSinglePlanet)
}