package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.compositionLocalOf

/**
 * A local animated content scope, that is provided along with each transition to the composable factory function.
 * @see AnimatedContentScope
 */
val LocalAnimatedContentScope = compositionLocalOf<AnimatedContentScope> {
    error(message = "AnimatedContentScope was not provided")
}
