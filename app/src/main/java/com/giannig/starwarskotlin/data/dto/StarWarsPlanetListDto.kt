package com.giannig.starwarskotlin.data.dto

import com.google.gson.annotations.SerializedName


data class StarWarsPlanetListDto(

    @SerializedName("results")
    val planets: List<StarWarsSinglePlanetDto>?
)