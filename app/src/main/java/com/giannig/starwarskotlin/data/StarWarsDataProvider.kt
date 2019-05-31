package com.giannig.starwarskotlin.data

import com.giannig.starwarskotlin.data.api.Api
import com.giannig.starwarskotlin.data.api.StarWarsApi
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanet
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object StarWarsDataProvider {

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .callTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(StarWarsApi::class.java)

    suspend fun providePlanets(): State {
        return try {
            retrofit.getPlanetListAsync().await().planets?.let {
                State.PlanetList(it)
            } ?: State.Error
        } catch (e: IOException) {
            State.NetworkError(e.localizedMessage)
        }
    }

    suspend fun provideSinglePlanet(planetId: String): State {
        return try {
            retrofit.getPlanetAsync(planetId).await().let {
                State.SinglePlanet(it)
            }
        } catch (e: IOException) {
            State.NetworkError(e.localizedMessage)
        }
    }
}

sealed class State {
    object Error : State()
    data class NetworkError(val message: String) : State()
    data class SinglePlanet(val planet: StarWarsSinglePlanet) : State()
    data class PlanetList(val planets: List<StarWarsSinglePlanet>) : State()
}