package com.yanadroid.kroute.navigation.router

import androidx.compose.runtime.Immutable
import com.yanadroid.kroute.navigation.transition.scope.TransitionScope

@Immutable
interface IDialogRouter<R : Any> : IRouter<DialogState<R>> {

    suspend fun open(route: R, configuration: TransitionScope.() -> Unit = {})

    suspend fun close(configuration: TransitionScope.() -> Unit = {})
}
