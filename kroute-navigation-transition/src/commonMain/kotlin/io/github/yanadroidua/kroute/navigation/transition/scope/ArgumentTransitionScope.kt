package io.github.yanadroidua.kroute.navigation.transition.scope

import io.github.yanadroidua.kroute.navigation.arguments.Argument
import io.github.yanadroidua.kroute.navigation.transition.TransitionDslMarker

/**
 * Scope to manipulate arguments of transition
 */
@TransitionDslMarker
interface ArgumentTransitionScope {
    /**
     * Puts a new argument into transition.
     *
     *  @param argument an argument to put
     *  @param value value of that argument
     */
    fun <T : Any> putArgument(argument: Argument<T>, value: T)

    /**
     * Removes an argument from transition.
     *
     * @param argument an argument to remove
     */
    fun <T : Any> removeArgument(argument: Argument<T>)

    /**
     * Gets an argument from transition, or `null` if transition does not have such argument.
     *
     *  @param argument an argument to search for
     *  @return value of the [argument] or `null` if argument is not present in transition
     */
    fun <T : Any> getArgumentOrNull(argument: Argument<T>): T?
}
