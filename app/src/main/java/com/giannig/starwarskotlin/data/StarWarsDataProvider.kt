package com.giannig.starwarskotlin.data

import com.giannig.starwarskotlin.data.api.Api
import com.giannig.starwarskotlin.data.api.StarWarsApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    suspend fun providePlanets() = retrofit
        .getPlanetListAsync().planets

    /**
     * Provides a single star wars planet planet
     */
    suspend fun provideSinglePlanet(planetId: String) = retrofit
        .getPlanetAsync(planetId)

}


