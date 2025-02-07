package io.github.yanadroidua.kroute.navigation.router

import io.github.yanadroidua.kroute.navigation.transition.Transition

/**
 * Represents an ongoing transition.
 * @param R route
 */
interface IOngoingOperation<R : Any> {
    /**
     * Transition that is currently ongoing
     */
    val transition: Transition<R>

    /**
     * `true` if this is a backward transition, `false` if it's a forward transition
     */
    val isBack: Boolean

    /**
     * Starts the transition
     */
    suspend fun start()
}
