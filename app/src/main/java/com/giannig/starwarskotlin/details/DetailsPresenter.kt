package com.giannig.starwarskotlin.details

import com.giannig.starwarskotlin.arch.StartStopPresenter
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.StarWarsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class DetailsPresenter(coroutineContext: CoroutineContext) :
    StartStopPresenter<StarWarsState>(coroutineContext) {

    override fun doOnAsync(view: ViewState<StarWarsState>) = launch {
        view.updateState(StarWarsState.Loading)
        val responseState = provideSinglePlanet("1")
        withContext(Dispatchers.Main) {
            view.updateState(responseState)
        }
    }

    private suspend fun provideSinglePlanet(planetId: String) = try {
        StarWarsDataProvider.provideSinglePlanet(planetId)
            .let {
                StarWarsState.SinglePlanet(it)
            }
    } catch (e: IOException) {
        StarWarsState.Error(e.localizedMessage)
    }

}
