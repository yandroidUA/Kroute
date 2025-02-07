package com.yanadroid.kroute.navigation.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable

@Immutable
data class Transition<R : Any>(
    val destination: R,
    val enterAnimation: EnterTransition = EnterTransition.None,
    val currentExitAnimation: ExitTransition = ExitTransition.None,
    val arguments: Map<String, Any> = emptyMap(),
    val expedite: Boolean = false,
)
