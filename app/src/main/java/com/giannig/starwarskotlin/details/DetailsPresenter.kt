package com.giannig.starwarskotlin.details

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.State
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import com.giannig.starwarskotlin.details.view.DetailsView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailsPresenter(private val view: DetailsView) : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun onStart() {
        view.loading()
    }

    fun loadPlanetDetails(planetId: String) {
        updateUIState(planetId)
    }


    fun onClose() {
        job.cancel()
    }

    private fun updateUIState(planetId: String) = launch {
        when (val responseState = StarWarsDataProvider.provideSinglePlanet(planetId)) {
            is State.SinglePlanet -> showPlanetView(responseState.planet)
            is State.NetworkError -> showErrorMessage(responseState.message)
            else -> showErrorMessage(null)
        }
    }

    private suspend fun showPlanetView(result: StarWarsSinglePlanetDto) = withContext(Dispatchers.Main) {
        view.showPlanetData(result)
    }

    private suspend fun showErrorMessage(message: String?) = withContext(Dispatchers.Main) {
        view.showErrorMessage(message)
    }
}