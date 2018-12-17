package com.giannig.starwarskotlin.main

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.State
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanet
import com.giannig.starwarskotlin.main.view.MainView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainView) : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + IO

    fun update() {
        loadData()
        view.loading()
    }

    fun onClose() {
        job.cancel()
    }

    private fun loadData() = launch {
        val responseState = StarWarsDataProvider.providePlanets()
        when (responseState) {
            is State.PlanetList -> updateUi(responseState.planets)
            is State.NetworkError -> showErrorMessage(responseState.message)
            else -> showErrorMessage(null)
        }
    }

    private suspend fun updateUi(result: List<StarWarsSinglePlanet>) = withContext(Main) {
        view.updateList(result)
        view.showItemList()
    }

    private suspend fun showErrorMessage(message: String?) = withContext(Main) {
        view.showErrorMessage(message)
    }
}