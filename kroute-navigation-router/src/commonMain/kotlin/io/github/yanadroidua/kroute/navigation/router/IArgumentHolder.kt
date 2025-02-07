package io.github.yanadroidua.kroute.navigation.router

import io.github.yanadroidua.kroute.navigation.arguments.Argument
import io.github.yanadroidua.kroute.navigation.transition.Transition

/**
 * Encapsulates some operations that can be done to the transition arguments.
 */
interface IArgumentHolder<R : Any> {
    /**
     * Removes an argument of out transition.
     * @param transition transition from which argument should be removed
     * @param argument argument that should be removed
     */
    fun <T : Any> onArgumentRemoved(transition: Transition<R>, argument: Argument<T>)
}
