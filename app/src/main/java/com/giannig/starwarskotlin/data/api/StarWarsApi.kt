package com.giannig.starwarskotlin.data.api

import com.giannig.starwarskotlin.data.dto.StarWarsPlanetListDto
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * swapi api
 */
interface StarWarsApi {

    /**
     * Request for the planets
     */
    @GET("planets")
    fun getPlanetListAsync(): Deferred<StarWarsPlanetListDto>

    /**
     * Request for a planet for a given "id"
     *
     * Note: in this API "id" means the position of the the planet list starting from 1
     * id = planets[3] + 1]
     */
    @GET("planets/{id}")
    fun getPlanetAsync(@Path("id") id: String): Deferred<StarWarsSinglePlanetDto>
}