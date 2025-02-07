package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.runtime.Immutable

/**
 * Represents all possible states of the dialog:
 *
 * - [Empty][DialogState.Empty] - nothing is rendered/present in the router
 * - [Present][DialogState.Present] - something should be rendered
 */
@Immutable
sealed interface DialogState<out R : Any> {
    /**
     * This state means that there is currently nothing present in the router, so nothing to render.
     */
    @Immutable
    data object Empty : DialogState<Nothing>

    /**
     * This state means that there is currently something present in the router and the dialog should be shown.
     * @param route current route
     */
    @Immutable
    data class Present<R : Any>(val route: R) : DialogState<R>
}
