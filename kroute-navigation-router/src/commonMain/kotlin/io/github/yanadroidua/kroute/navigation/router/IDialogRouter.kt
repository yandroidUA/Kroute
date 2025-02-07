package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.runtime.Immutable
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

/**
 * A router that has an empty state and does not have a history of routes.
 * @see IRouter
 */
@Immutable
interface IDialogRouter<R : Any> : IRouter<DialogState<R>> {
    /**
     * Pushes a route to the currently selected one. Route will be packed into [DialogState.Present].
     *
     * @param route route/dialog
     * @param configuration transition configuration
     * @see TransitionScope
     */
    suspend fun open(route: R, configuration: TransitionScope.() -> Unit = {})

    /**
     * Closes current route, by utilizing [DialogState.Empty].
     * @param configuration transition configuration
     * @see TransitionScope
     */
    suspend fun close(configuration: TransitionScope.() -> Unit = {})

    companion object {
        /**
         * Instantiates a new [dialog router][IDialogRouter].
         *
         * @see IRouteExit
         * @see IDialogRouter
         */
        fun <R : Any> router(): IDialogRouter<R> = DialogRouterImpl()
    }
}
