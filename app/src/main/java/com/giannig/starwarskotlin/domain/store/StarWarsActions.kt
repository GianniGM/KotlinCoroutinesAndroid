package com.giannig.starwarskotlin.domain.store

import com.giannig.starwarskotlin.libs.redux.Action

/**
 * State returned from api request
 */
sealed class StarWarsActions : Action {
    object FetchPlanetList : StarWarsActions()
    data class FetchSinglePlanet(val planetId: String) : StarWarsActions()
}