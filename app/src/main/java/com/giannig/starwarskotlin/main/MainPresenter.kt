package com.giannig.starwarskotlin.main

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.State
import com.giannig.starwarskotlin.data.dto.StarWarsSinglePlanetDto
import com.giannig.starwarskotlin.main.view.MainView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Presnter of the MainView
 */
class MainPresenter(private val view: MainView) : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + IO

    fun onStart() {
        update()
    }

    fun update() {
        updateUIState()
        view.loadView()
    }

    fun onClose() {
        job.cancel()
    }

    private fun updateUIState() = launch {
        //Coroutine Scope
        when (val responseState = StarWarsDataProvider.providePlanets()) {
            is State.PlanetList -> showPlanetList(responseState.planets)
            is State.NetworkError -> showErrorMessage(responseState.message)
            else -> showErrorMessage("can't load")
        }
    }

    private suspend fun showPlanetList(result: List<StarWarsSinglePlanetDto>) = withContext(Main) {
        view.updateList(result)
        view.showItemList()
    }

    private suspend fun showErrorMessage(message: String?) = withContext(Main) {
        view.showErrorMessage(message)
    }
}