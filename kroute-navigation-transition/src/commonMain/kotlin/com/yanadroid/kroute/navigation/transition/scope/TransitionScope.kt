package com.yanadroid.kroute.navigation.transition.scope

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.yanadroid.kroute.navigation.transition.TransitionDslMarker

@TransitionDslMarker
interface TransitionScope : ArgumentTransitionScope {

    fun expedite(expedite: Boolean)

    fun enterAnimation(enterAnimation: EnterTransition)

    fun currentExitAnimation(exitAnimation: ExitTransition)
}
