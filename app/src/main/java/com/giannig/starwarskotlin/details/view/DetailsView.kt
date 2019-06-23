package com.giannig.starwarskotlin.details.view

import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto

/**
 * View of Planets Details
 */
interface DetailsView {

    /**
     * Set Loading View
     */
    fun loading()

    /**
     * Show Error message
     */
    fun showErrorMessage(message: String?)

    /**
     * Show planet data
     */
    fun showPlanetData(planet: StarWarsSinglePlanetDto)
}