package com.giannig.starwarskotlin.arch

/**
 * View of Planets Details
 */
interface ViewState<S: State> {

    /**
     * update the state of the ui
     */
    fun updateState(state: S)

}