package com.giannig.starwarskotlin.main

import com.giannig.starwarskotlin.arch.Presenter
import com.giannig.starwarskotlin.arch.ViewState
import com.giannig.starwarskotlin.data.StarWarsDataProvider
import com.giannig.starwarskotlin.data.StarWarsState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

/**
 * Presnter of the MainView
 */
class MainPresenter(
    coroutineContext: CoroutineContext = Job() + IO
) : Presenter<StarWarsState>(coroutineContext){

    override fun doOnAsync(view: ViewState<StarWarsState>) = launch {
        view.updateState(StarWarsState.Loading)
        val responseState = StarWarsDataProvider.providePlanets()
        withContext(Main) {
            view.updateState(responseState)
        }
    }
}