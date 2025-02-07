package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.runtime.Immutable
import io.github.yanadroidua.kroute.navigation.transition.Transition
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a base router, with the current route and ongoing transition.
 */
@Immutable
interface IRouter<R : Any> : IArgumentHolder<R> {
    /**
     * Transition state, always holds the current one.
     */
    val currentTransition: StateFlow<Transition<R>>

    /**
     * Ongoing receiver channel.
     *
     * Once a new transition has been started, it will be firstly pushed to this channel to kick-off transition UI.
     */
    val ongoingOperation: ReceiveChannel<IOngoingOperation<R>>

    /**
     * `true` if router is currently performing an operation, otherwise - `false`
     */
    val isPerformingOperation: Boolean
}
