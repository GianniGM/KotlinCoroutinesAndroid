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
    suspend fun providePlanets(): State = try {
        retrofit
            .getPlanetListAsync()
            .await()
            .planets?.let {
            State.PlanetList(it)
        } ?: State.Error
    } catch (e: IOException) {
        State.NetworkError(e.localizedMessage)
    }

//fun call() {
//    val call: Call<List<StarWarsSinglePlanetDto>> = retrofit.getPlanetListAsync()
//
//    call.enqueue(object : Callback<List<StarWarsSinglePlanetDto>> {
//        override fun onFailure(call: Call<List<StarWarsSinglePlanetDto>>, t: Throwable) {
//            // handle succes
//        }
//
//        override fun onResponse(
//            call: Call<List<StarWarsSinglePlanetDto>>,
//            response: Response<List<StarWarsSinglePlanetDto>>
//        ) {
//            // handle failure
//        }
//
//    })
//}


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


