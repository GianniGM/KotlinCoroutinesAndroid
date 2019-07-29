package com.giannig.starwarskotlin.libs.redux

/**
 * View of Planets Details
 */
interface ViewState<S: State> {

    /**
     * update the state of the ui
     */
    fun updateState(state: S)

}