package com.yanadroid.kroute.navigation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.compositionLocalOf

val LocalAnimatedContentScope = compositionLocalOf<AnimatedContentScope> {
    error(message = "AnimatedContentScope was not provided")
}
