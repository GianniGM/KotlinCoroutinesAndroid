package com.giannig.starwarskotlin.details

import com.giannig.starwarskotlin.arch.Presenter
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.StarWarsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DetailsPresenter(coroutineContext: CoroutineContext) :
    Presenter<StarWarsState>(coroutineContext) {

    override fun doOnAsync(view: ViewState<StarWarsState>) = launch {
        view.updateState(StarWarsState.Loading)
        val responseState = StarWarsDataProvider.provideSinglePlanet("1")
        withContext(Dispatchers.Main) {
            view.updateState(responseState)
        }
    }
}
