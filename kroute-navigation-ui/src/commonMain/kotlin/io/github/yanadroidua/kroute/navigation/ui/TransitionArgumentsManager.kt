package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.runtime.Immutable
import io.github.aakira.napier.Napier
import io.github.yanadroidua.kroute.navigation.arguments.Argument
import io.github.yanadroidua.kroute.navigation.arguments.ArgumentLifecycle
import io.github.yanadroidua.kroute.navigation.arguments.ITransitionArguments
import io.github.yanadroidua.kroute.navigation.router.IArgumentHolder
import io.github.yanadroidua.kroute.navigation.transition.Transition

@Immutable
internal class TransitionArgumentsManager<R : Any>(
    private val transition: Transition<R>,
    private val argumentHolder: IArgumentHolder<R>,
) : ITransitionArguments {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getOrNull(argument: Argument<T>): T? {
        val value = transition.arguments[argument.storageKey]

        return try {
            value?.let {
                val castedValue = it as? T

                when (argument.lifecycle) {
                    ArgumentLifecycle.ONE_TIME -> argumentHolder.onArgumentRemoved(transition, argument)
                    ArgumentLifecycle.PERMANENT -> {}
                }

                castedValue
            }
        } catch (e: Exception) {
            Napier.e(throwable = e) { "Failed to get an argument ${argument.storageKey} with a type ${argument.type.simpleName}" }
            null
        }
    }

    override fun <T : Any> exists(argument: Argument<T>): Boolean = transition.arguments.containsKey(argument.storageKey)
}
