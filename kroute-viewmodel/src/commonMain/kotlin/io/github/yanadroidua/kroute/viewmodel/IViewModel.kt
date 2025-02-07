package io.github.yanadroidua.kroute.viewmodel

/**
 * Represents a view model object that stores view data and handles user interactions.
 */
interface IViewModel {

    /**
     * `true` if ViewModel is currently active and can perform tasks.
     */
    val isActive: Boolean

    /**
     * `true` if ViewModel has been paused and all tasks have been cancelled.
     */
    val isPaused: Boolean

    /**
     *  `true` if this ViewModel instance has been destroyed or about to be destroyed and removed from the heap.
     */
    val isAboutToBeDestroyed: Boolean

    /**
     * Invokes once view model become active on the screen.
     */
    fun onStart()

    /**
     * Invokes once view model become inactive.
     */
    fun onPause()

    /**
     * Invokes once view model is completely destroyed.
     */
    fun onDestroy()
}
