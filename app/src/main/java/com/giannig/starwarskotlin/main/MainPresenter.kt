package com.giannig.starwarskotlin.main

import com.giannig.starwarskotlin.arch.StartStopPresenter
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.StarWarsDataProvider.providePlanets
import com.giannig.starwarskotlin.data.StarWarsState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * Presenter of the MainView
 */
class MainPresenter(
    coroutineContext: CoroutineContext = Job() + IO
) : StartStopPresenter<StarWarsState>(coroutineContext) {

    override fun doOnAsync(view: ViewState<StarWarsState>) = launch {
        view.updateState(StarWarsState.Loading)
        val responseState = providePlanets()
        withContext(Main) {
            view.updateState(responseState)
        }
    }

    private suspend fun providePlanets(): StarWarsState = try {
        StarWarsDataProvider
            .providePlanets()
            .let {
                StarWarsState.PlanetList(it)
            }
    } catch (e: IOException) {
        StarWarsState.Error(e.localizedMessage)
    }
}