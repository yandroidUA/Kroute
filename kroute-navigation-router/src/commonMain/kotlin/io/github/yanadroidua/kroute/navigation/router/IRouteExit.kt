package io.github.yanadroidua.kroute.navigation.router

import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

/**
 * Represent an exit of the router.
 */
fun interface IRouteExit {
    /**
     * Called by a [IScreenRouter] when there are no transitions left in the back stack.
     * @param configuration transition configuration
     * @see TransitionScope
     */
    suspend fun onExit(configuration: TransitionScope.() -> Unit)
}
