package com.giannig.starwarskotlin.libs.redux

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlin.coroutines.CoroutineContext


abstract class StarStopReducer<A : Action, S : State>(
    private val coroutineContextJob: CoroutineContext = Job() + Dispatchers.IO
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = coroutineContextJob

    private lateinit var receiveChannel: ReceiveChannel<A>
    private lateinit var sendingChannel: SendChannel<A>

    fun onClose() {
        sendingChannel.close()
        coroutineContext.cancel()
    }

    @ExperimentalCoroutinesApi
    fun onStart(view: ViewState<S>) {
        val channel = Channel<A>()
        this.sendingChannel = channel
        this.receiveChannel = channel

        onActionReceived(view)
    }

    fun sendAction(action: A) = launch {
        sendingChannel.send(action)
    }

    @ExperimentalCoroutinesApi
    private fun onActionReceived(view: ViewState<S>) = launch {
        while (!receiveChannel.isClosedForReceive) {

            val action: A? = try {
                receiveChannel.receive()
            } catch (e: ClosedReceiveChannelException) {
                null
            }

            action?.let {
                onPreReduceAsync(view)
                onPreReduce(view)
                val state = reduce(action)
                logActionState(action, state)
                onReduceComplete(view, state)
            }
        }
    }

    private fun logActionState(action: A, state: S) {
        Log.d(
            TAG,
            "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
        )
        Log.d(TAG, "action: $action")
        Log.d(TAG, "to State => $state ")
        Log.d(
            TAG,
            "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
        )
    }

    private fun logClosedChannel(){
        Log.d(
            TAG,
            "++++++++++++++++++++++++++++++++++++++++++END+OF+ACTIONS++++++++++++++++++++++++++++++++++++++++++++"
        )
    }


    protected open suspend fun onPreReduce(view: ViewState<S>) {
        /* override it if you want to make operation before reducing stuff */
    }

    protected open suspend fun onPreReduceAsync(view: ViewState<S>) = launch {
        /* override it if you want to make operation before reducing stuff asynchronously*/
    }

    protected abstract suspend fun reduce(action: A): S
    protected abstract suspend fun onReduceComplete(view: ViewState<S>, state: S)

    companion object {
        const val TAG = "STATE_UPDATED"
    }
}