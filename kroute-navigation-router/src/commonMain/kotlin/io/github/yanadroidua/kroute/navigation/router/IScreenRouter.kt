package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.runtime.Immutable
import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

/**
 * Represents a screen router, with a backstack and functions to manipulate it.
 * @see IRouter
 */
@Immutable
interface IScreenRouter<R : Any> : IRouter<R> {
    /**
     * Pushes a new route to the top of the stack and kick-offs an operation to start a transition to that route.
     *
     * @param route route to navigate to
     * @param configuration transition configuration
     */
    suspend fun goTo(route: R, configuration: TransitionScope.() -> Unit = {})

    /**
     * Puts passed transitions in the backstack and pushes the route to the top of the stack and kick-offs an operation to start a transition to that route.
     *
     * @param route route to navigate to
     * @param backStack list of transitions that should be placed **before** the route
     * @param configuration transition configuration
     */
    suspend fun goToWithBackStack(
        route: R,
        backStack: List<Transition<R>>,
        configuration: TransitionScope.() -> Unit = {},
    )

    /**
     * Pops the top route out of the stack and starts the transition to the previous route. If there are no routes left
     * in the stack, it calls [IRouteExit].
     *
     * @param configuration transition configuration
     */
    suspend fun back(configuration: TransitionScope.() -> Unit = {}): Boolean

    /**
     * Pops the stack until it finds a mentioned route and starts a transition to it, if route has not been found - nothing happens.
     *
     * @param route route that should be on top (aka. visible)
     * @param configuration transition configuration
     */
    suspend fun popTo(route: R, configuration: TransitionScope.() -> Unit = {}): Boolean

    /**
     * Replaces **entire** stack with the route, so it becomes a root and starts a transition to it.
     *
     * @param route root route
     * @param configuration transition configuration
     */
    suspend fun replaceAll(route: R, configuration: TransitionScope.() -> Unit = {})

    /**
     * Replaces **entire** stack with the route, additionally puts a list of transitions into the backstack, so the
     * first transition in the list becomes new root.
     *
     * @param route root route
     * @param backStack list of transitions that should be placed **before** the route
     * @param configuration transition configuration
     */
    suspend fun replaceAllWithBackStack(
        route: R,
        backStack: List<Transition<R>>,
        configuration: TransitionScope.() -> Unit = {},
    )

    /**
     * Searches the route in the backstack.
     *
     * @param route route to check
     * @return `true` if route has been found in the back stack or `false` if it was not.
     */
    suspend fun isInStack(route: R): Boolean

    /**
     * Adds a listener that notified on each stack change.
     * @see IStackListener
     */
    fun addListener(listener: IStackListener<R>)

    /**
     * Removes a listener.
     * @see IStackListener
     */
    fun removeListener(listener: IStackListener<R>)

    companion object {
        /**
         * Instantiates a new [screen router][IScreenRouter].
         *
         * @param initialRoute initial route of the router
         * @param routeExit an exit of the route
         * @see IRouteExit
         * @see IScreenRouter
         */
        fun <R : Any> router(
            initialRoute: R,
            routeExit: IRouteExit,
        ): IScreenRouter<R> = ScreenRouterImpl(
            initialDestination = initialRoute,
            routeExit = routeExit,
        )
    }
}
