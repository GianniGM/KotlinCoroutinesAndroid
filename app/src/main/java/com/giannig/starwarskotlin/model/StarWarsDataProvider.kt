package com.giannig.starwarskotlin.model

import com.giannig.starwarskotlin.api.Api
import com.giannig.starwarskotlin.api.StarWarsApiKC
import com.giannig.starwarskotlin.model.dto.StarWarsPlanet
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class StarWarsDataProvider {

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build().create(StarWarsApiKC::class.java)

    suspend fun providePlanets(): State {
        return try {
            val planets = retrofit.getPlanets().await()
            State.Success(planets.results)
        }catch (e: IOException){
            State.Error
        }
    }
}

sealed class State {
    data class Success(val result: List<StarWarsPlanet>?): State()
    object Error : State()
}