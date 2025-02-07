package io.github.yanadroidua.kroute.navigation.router.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import io.github.yanadroidua.kroute.navigation.transition.Transition

internal fun Transition<*>.withTestId() = copy(id = "TEST")

internal fun List<Transition<*>>.withTestId() = map { it.copy(id = "TEST") }

internal fun <T : Any> transition(
    destination: T,
    enterAnimation: EnterTransition = EnterTransition.None,
    currentExitAnimation: ExitTransition = ExitTransition.None,
    arguments: Map<String, Any> = emptyMap(),
) = Transition(
    id = "TEST",
    destination = destination,
    arguments = arguments,
    currentExitAnimation = currentExitAnimation,
    enterAnimation = enterAnimation,
)
