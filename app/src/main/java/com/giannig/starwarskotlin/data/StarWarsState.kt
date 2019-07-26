package com.giannig.starwarskotlin.data

import com.giannig.starwarskotlin.arch.State
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto

/**
 * State returned from api request
 */
sealed class StarWarsState: State {
    object Error : StarWarsState()
    object Loading: StarWarsState()
    data class NetworkError(val message: String) : StarWarsState()
    data class SinglePlanet(val planet: StarWarsSinglePlanetDto) : StarWarsState()
    data class PlanetList(val planets: List<StarWarsSinglePlanetDto>) : StarWarsState()
}