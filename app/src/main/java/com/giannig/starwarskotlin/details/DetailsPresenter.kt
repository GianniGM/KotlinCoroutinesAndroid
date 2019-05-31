package com.giannig.starwarskotlin.details

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.State
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanet
import com.giannig.starwarskotlin.details.view.DetailsActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailsPresenter(private val view: DetailsActivity) : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun onClose() {
        job.cancel()
    }

    fun dispatchData(planetId: String) = launch {
        when (val responseState = StarWarsDataProvider.provideSinglePlanet(planetId)) {
            is State.SinglePlanet -> updateData(responseState.planet)
            is State.NetworkError -> updateErrorMessage(responseState.message)
            else -> updateErrorMessage(null)
        }
    }

    private suspend fun updateData(result: StarWarsSinglePlanet) = withContext(Dispatchers.Main) {
        view.showData(result)
    }

    private suspend fun updateErrorMessage(message: String?) = withContext(Dispatchers.Main) {
        view.showErrorMessage(message)
    }
}