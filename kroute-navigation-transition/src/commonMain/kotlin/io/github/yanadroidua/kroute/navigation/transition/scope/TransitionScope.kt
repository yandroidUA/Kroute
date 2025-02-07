package io.github.yanadroidua.kroute.navigation.transition.scope

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import io.github.yanadroidua.kroute.navigation.transition.TransitionDslMarker

/**
 * Scope to customize transition.
 */
@TransitionDslMarker
interface TransitionScope : ArgumentTransitionScope {
    /**
     * Ignore all possible animations/delays and play transition immediately without any animation.
     *
     *  @param expedite `true` to enable expedite mode, `false` - to disable
     */
    fun expedite(expedite: Boolean)

    /**
     * Configures enter animation of the transition.
     *
     * @param enterAnimation animation
     */
    fun enterAnimation(enterAnimation: EnterTransition)

    /**
     * Configures exit animation of the transition.
     *
     * @param exitAnimation animation
     */
    fun currentExitAnimation(exitAnimation: ExitTransition)
}
