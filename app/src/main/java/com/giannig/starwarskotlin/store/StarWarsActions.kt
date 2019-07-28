package com.giannig.starwarskotlin.store

import com.giannig.starwarskotlin.redux.Action

/**
 * State returned from api request
 */
sealed class StarWarsActions : Action {
    object FetchPlanetList : StarWarsActions()
    data class FetchSinglePlanet(val planetId: String) : StarWarsActions()
}