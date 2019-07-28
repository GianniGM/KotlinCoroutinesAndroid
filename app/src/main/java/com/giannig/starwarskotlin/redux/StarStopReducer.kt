package com.giannig.starwarskotlin.redux

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext


abstract class StarStopReducer<A : Action, S : State>(
    private val coroutineContextJob: CoroutineContext = Job() + Dispatchers.IO
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = coroutineContextJob

    lateinit var channel: Channel<A>

    fun onClose() {
        channel.close()
        coroutineContext.cancel()
    }

    fun onStart(view: ViewState<S>) {
        channel = Channel()
        receiveAction(view)

    }

    fun sendAction(action: A) = launch {
        channel.send(action)
    }

    private fun receiveAction(view: ViewState<S>) = launch {
        onPreReduce(view)
        val action = channel.receive()
        val state = reduce(action)
        Log.d(TAG, "action : $action -> $state ")
        onReduceComplete(view, state)
    }


    protected open fun onPreReduce(view: ViewState<S>) {
        /* override it if you want to make operation before reducing stuff */
    }

    protected open fun onPreReduceAsync(view: ViewState<S>) = launch {
        /* override it if you want to make operation before reducing stuff asynchronously*/
    }

    protected abstract suspend fun reduce(action: A): S
    protected abstract suspend fun onReduceComplete(view: ViewState<S>, state: S)

    companion object {
        const val TAG = "STATE_UPDATED"
    }
}