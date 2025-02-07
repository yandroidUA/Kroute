package io.github.yanadroidua.kroute.navigation.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import io.github.yanadroidua.kroute.navigation.arguments.Argument
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Transition builder that is used to instantiate a new transition and configure it. It's also used to copy and tweak
 * existing transitions.
 *
 * @param destination route/destination of the transition
 */
class TransitionBuilder<R : Any>(private val destination: R) : TransitionScope {

    private var id: String = id(destination = destination)
    private val arguments = mutableMapOf<String, Any>()
    private var enterAnimation: EnterTransition = EnterTransition.None
    private var exitAnimation: ExitTransition = ExitTransition.None
    private var expedite: Boolean = false

    /**
     * Copies passed transition and exposes it to configuration.
     * **[Transition.expedite] is not get copied.**
     *
     * @param transition transition to copy
     */
    constructor(transition: Transition<R>) : this(
        destination = transition.destination,
    ) {
        this.id = transition.id
        this.enterAnimation = transition.enterAnimation
        this.exitAnimation = transition.currentExitAnimation
        this.arguments.putAll(transition.arguments)
    }

    /**
     * Builds a final immutable transition applying all configurations.
     * @see Transition
     */
    fun build(): Transition<R> = Transition(
        id = id,
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

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun <T : Any> id(destination: T): String = "${Uuid.random()}-$destination"

        fun <T : Any> build(destination: T): Transition<T> = TransitionBuilder(destination = destination).build()
    }
}
