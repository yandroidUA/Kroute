package com.yanadroid.kroute.navigation.router

import androidx.compose.runtime.Immutable
import com.yanadroid.kroute.navigation.transition.Transition
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

@Immutable
interface IRouter<R : Any> : IArgumentHolder<R> {

    val currentTransition: StateFlow<Transition<R>>

    val ongoingOperation: ReceiveChannel<IOngoingOperation<R>>

    val isPerformingOperation: Boolean
}
