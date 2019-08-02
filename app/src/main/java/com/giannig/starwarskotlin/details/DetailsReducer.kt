package com.giannig.starwarskotlin.details

import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.domain.store.StarWarsActions
import com.giannig.starwarskotlin.domain.store.StarWarsState
import com.giannig.starwarskotlin.libs.redux.StarStopReducer
import com.giannig.starwarskotlin.libs.redux.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class DetailsReducer(coroutineContext: CoroutineContext) :
    StarStopReducer<StarWarsActions, StarWarsState>(coroutineContext) {

    override suspend fun onPreReduce(view: ViewState<StarWarsState>) {
        view.updateState(StarWarsState.Loading)
    }

    override suspend fun reduce(action: StarWarsActions) = when (action) {
        StarWarsActions.FetchPlanetList -> StarWarsState.Error("unexpected error")
        is StarWarsActions.FetchSinglePlanet -> provideState(action.planetId)
    }

    override suspend fun onReduceComplete(view: ViewState<StarWarsState>, state: StarWarsState) =
        withContext(Dispatchers.Main) {
            view.updateState(state)
        }

    private suspend fun provideState(planetId: String) = try {
        StarWarsDataProvider.provideSinglePlanet(planetId)
            .let {
                StarWarsState.SinglePlanet(it)
            }
    } catch (e: IOException) {
        StarWarsState.Error(e.localizedMessage)
    }
}
