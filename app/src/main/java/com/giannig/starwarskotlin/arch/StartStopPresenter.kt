package com.giannig.starwarskotlin.arch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext


abstract class StartStopPresenter<S : State>(
    private val coroutineContextJob: CoroutineContext = Job() + Dispatchers.IO
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = coroutineContextJob

    fun onClose() {
        coroutineContext.cancel()
    }

    fun onStart(view: ViewState<S>) {
        doOnAsync(view)
    }

    protected abstract fun doOnAsync(view: ViewState<S>): Job
}