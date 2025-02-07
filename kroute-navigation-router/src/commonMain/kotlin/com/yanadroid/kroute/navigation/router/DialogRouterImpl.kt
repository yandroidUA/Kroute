package com.yanadroid.kroute.navigation.router

import com.yanadroid.kroute.navigation.arguments.Argument
import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.navigation.transition.TransitionBuilder
import com.yanadroid.kroute.navigation.transition.scope.TransitionScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DialogRouterImpl<R : Any> : IDialogRouter<R> {

    private val currentDialog = MutableStateFlow<Transition<DialogState<R>>>(
        Transition(DialogState.Empty),
    )

    private val ongoingOperationChannel = Channel<IOngoingOperation<DialogState<R>>>(
        capacity = Channel.RENDEZVOUS,
        onBufferOverflow = BufferOverflow.SUSPEND,
    )

    override val ongoingOperation: ReceiveChannel<IOngoingOperation<DialogState<R>>> = ongoingOperationChannel

    override val currentTransition: StateFlow<Transition<DialogState<R>>> = currentDialog.asStateFlow()

    override val isPerformingOperation: Boolean
        get() = false

    override suspend fun close(configuration: TransitionScope.() -> Unit) {
        val transition = TransitionBuilder<DialogState<R>>(DialogState.Empty).apply(configuration).build()
        ongoingOperationChannel.send(CloseOngoingOperation(transition))
    }

    override suspend fun open(route: R, configuration: TransitionScope.() -> Unit) {
        val transition = TransitionBuilder<DialogState<R>>(DialogState.Present(route)).apply(configuration).build()
        ongoingOperationChannel.send(ForwardOngoingOperation(transition))
    }

    override fun <T : Any> onArgumentRemoved(transition: Transition<DialogState<R>>, argument: Argument<T>) = Unit

    private inner class ForwardOngoingOperation(override val transition: Transition<DialogState<R>>) : IOngoingOperation<DialogState<R>> {
        override val isBack: Boolean = false

        override suspend fun start() {
            currentDialog.value = transition
        }
    }

    private inner class CloseOngoingOperation(override val transition: Transition<DialogState<R>>) : IOngoingOperation<DialogState<R>> {
        override val isBack: Boolean = true

        override suspend fun start() {
            currentDialog.value = transition
        }
    }
}
