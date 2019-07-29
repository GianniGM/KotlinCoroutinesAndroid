package com.giannig.starwarskotlin.main

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.domain.store.StarWarsActions
import com.giannig.starwarskotlin.domain.store.StarWarsState
import com.giannig.starwarskotlin.libs.redux.StarStopReducer
import com.giannig.starwarskotlin.libs.redux.ViewState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Presenter of the MainView
 */
class MainReducer : StarStopReducer<StarWarsActions, StarWarsState>() {

    override fun onPreReduce(view: ViewState<StarWarsState>) {
        view.updateState(StarWarsState.Loading)
    }

    override suspend fun reduce(action: StarWarsActions) = when (action) {
        StarWarsActions.FetchPlanetList -> providePlanets()
        is StarWarsActions.FetchSinglePlanet -> StarWarsState.Error("unexpected error")
    }

    override suspend fun onReduceComplete(view: ViewState<StarWarsState>, state: StarWarsState) {
        withContext(Main) {
            view.updateState(state)
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