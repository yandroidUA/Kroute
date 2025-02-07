package io.github.yanadroidua.kroute.navigation.router

import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.navigation.transition.TransitionBuilder
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

/**
 * A helper function to build a new [Transition], by specifying a route and optional configuration.
 *
 * @param route route/destination of the transition
 * @param configuration transition configuration
 */
fun <R : Any> transition(route: R, configuration: TransitionScope.() -> Unit = {}): Transition<R> =
    TransitionBuilder(route)
        .apply(configuration)
        .build()
