package com.giannig.starwarskotlin.data

import com.giannig.starwarskotlin.arch.Action

/**
 * State returned from api request
 */
sealed class StarWarsActions : Action {
    object FetchPlanetList : StarWarsActions()
    data class FetchSinglePlanet(val planetId: String) : StarWarsActions()
}