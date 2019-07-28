package com.giannig.starwarskotlin.details

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.redux.StarStopReducer
import com.giannig.starwarskotlin.redux.ViewState
import com.giannig.starwarskotlin.store.StarWarsActions
import com.giannig.starwarskotlin.store.StarWarsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class DetailsReducer(coroutineContext: CoroutineContext) :
    StarStopReducer<StarWarsActions, StarWarsState>(coroutineContext) {

    override fun onPreReduce(view: ViewState<StarWarsState>) {
        view.updateState(StarWarsState.Loading)
    }

    override suspend fun reduce(action: StarWarsActions) = when (action) {
        StarWarsActions.FetchPlanetList -> StarWarsState.Error("unexpected error")
        is StarWarsActions.FetchSinglePlanet -> provideSinglePlanet(action.planetId)
    }

    override suspend fun onReduceComplete(view: ViewState<StarWarsState>, state: StarWarsState) {
        withContext(Dispatchers.Main) {
            view.updateState(state)
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
