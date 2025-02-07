package com.yanadroid.kroute.navigation.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.yanadroid.kroute.navigation.arguments.Argument
import com.yanadroid.kroute.navigation.transition.scope.TransitionScope

class TransitionBuilder<R : Any>(private val destination: R) : TransitionScope {

    private val arguments = mutableMapOf<String, Any>()
    private var enterAnimation: EnterTransition = EnterTransition.None
    private var exitAnimation: ExitTransition = ExitTransition.None
    private var expedite: Boolean = false

    constructor(transition: Transition<R>) : this(
        destination = transition.destination,
    ) {
        this.enterAnimation = transition.enterAnimation
        this.exitAnimation = transition.currentExitAnimation
        this.arguments.putAll(transition.arguments)
    }

    fun build(): Transition<R> = Transition(
        destination = destination,
        arguments = arguments,
        enterAnimation = enterAnimation,
        currentExitAnimation = exitAnimation,
        expedite = expedite,
    )

    override fun enterAnimation(enterAnimation: EnterTransition) {
        this.enterAnimation = enterAnimation
    }

    override fun currentExitAnimation(exitAnimation: ExitTransition) {
        this.exitAnimation = exitAnimation
    }

    override fun <T : Any> putArgument(argument: Argument<T>, value: T) {
        arguments[argument.storageKey] = value
    }

    override fun <T : Any> removeArgument(argument: Argument<T>) {
        arguments.remove(argument.storageKey)
    }

    override fun expedite(expedite: Boolean) {
        this.expedite = expedite
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getArgumentOrNull(argument: Argument<T>): T? = runCatching {
        arguments[argument.storageKey] as? T
    }.getOrNull()
}
