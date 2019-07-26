package com.giannig.starwarskotlin.data

import com.giannig.starwarskotlin.data.api.Api
import com.giannig.starwarskotlin.data.api.StarWarsApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Provides all the necessary to connect to Apis
 */
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

    /**
     * Provides all the planet list
     */
    suspend fun providePlanets(): StarWarsState = try {
        retrofit
            .getPlanetListAsync()
            .await()
            .planets?.let {
            StarWarsState.PlanetList(it)
        } ?: StarWarsState.Error
    } catch (e: IOException) {
        StarWarsState.NetworkError(e.localizedMessage)
    }

    suspend fun provideSinglePlanet(planetId: String): StarWarsState {
        return try {
            retrofit.getPlanetAsync(planetId).await().let {
                StarWarsState.SinglePlanet(it)
            }
        } catch (e: IOException) {
            StarWarsState.NetworkError(e.localizedMessage)
        }
    }
}


