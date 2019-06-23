package com.giannig.starwarskotlin.data

import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto

/**
 * State returned from api request
 */
sealed class State {
    object Error : State()
    data class NetworkError(val message: String) : State()
    data class SinglePlanet(val planet: StarWarsSinglePlanetDto) : State()
    data class PlanetList(val planets: List<StarWarsSinglePlanetDto>) : State()
}