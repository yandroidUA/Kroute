package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.animation.AnimatedVisibilityScope

/**
 * Navigation scope that is supplied with each transition to the composables.
 * @see AnimatedVisibilityScope
 */
interface NavigationViewScope<R : Any> : AnimatedVisibilityScope {
    /**
     * ID of the current transition
     */
    val transitionId: String

    /**
     * Destination/route of the current transition
     */
    val type: R
}

internal class NavigationScopeImpl<R : Any>(
    override val transitionId: String,
    override val type: R,
    animatedVisibilityScope: AnimatedVisibilityScope,
) : NavigationViewScope<R>,
    AnimatedVisibilityScope by animatedVisibilityScope
