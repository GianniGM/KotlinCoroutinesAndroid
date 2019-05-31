package com.giannig.starwarskotlin.data.api

import com.giannig.starwarskotlin.data.dto.StarWarsPlanetList
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanet
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface StarWarsApi {
    @GET("planets")
    fun getPlanetListAsync(): Deferred<StarWarsPlanetList>

    @GET("planets/{id}")
    fun getPlanetAsync(@Path("id") id: String): Deferred<StarWarsSinglePlanet>
}