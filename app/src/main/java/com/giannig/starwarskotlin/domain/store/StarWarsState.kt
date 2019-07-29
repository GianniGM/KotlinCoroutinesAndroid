package com.giannig.starwarskotlin.domain.store

import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import com.giannig.starwarskotlin.libs.redux.State

/**
 * State returned from api request
 */
sealed class StarWarsState: State {
    object Loading: StarWarsState()
    data class Error(val message: String) : StarWarsState()
    data class SinglePlanet(val planet: StarWarsSinglePlanetDto) : StarWarsState()
    data class PlanetList(val planets: List<StarWarsSinglePlanetDto>) : StarWarsState()
}