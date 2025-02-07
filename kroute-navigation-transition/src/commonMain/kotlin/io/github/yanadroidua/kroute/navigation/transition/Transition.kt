package io.github.yanadroidua.kroute.navigation.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable

/**
 * Represents a transition to the route.
 *
 * @param id transition identifier
 * @param destination route/destination which this transition leads to
 * @param enterAnimation enter animation of the transition, defaults to [EnterTransition.None]
 * @param currentExitAnimation exit animation of the previous route/destination, defaults to [ExitTransition.None]
 * @param arguments transition's arguments, defaults to an empty map
 * @param expedite whether expedite mode is enabled on this transition, defaults to `false`
 */
@Immutable
data class Transition<R : Any>(
    val id: String,
    val destination: R,
    val enterAnimation: EnterTransition = EnterTransition.None,
    val currentExitAnimation: ExitTransition = ExitTransition.None,
    val arguments: Map<String, Any> = emptyMap(),
    val expedite: Boolean = false,
)
