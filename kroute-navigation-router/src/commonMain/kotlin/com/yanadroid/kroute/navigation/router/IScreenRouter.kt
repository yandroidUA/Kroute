package com.yanadroid.kroute.navigation.router

import androidx.compose.runtime.Immutable
import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.navigation.transition.scope.TransitionScope

@Immutable
interface IScreenRouter<R : Any> : IRouter<R> {

    suspend fun goTo(route: R, configuration: TransitionScope.() -> Unit = {})

    suspend fun goToWithBackStack(
        route: R,
        backStack: List<Transition<R>>,
        configuration: TransitionScope.() -> Unit = {},
    )

    suspend fun back(configuration: TransitionScope.() -> Unit = {}): Boolean

    suspend fun popTo(route: R, configuration: TransitionScope.() -> Unit = {}): Boolean

    suspend fun replaceAll(route: R, configuration: TransitionScope.() -> Unit = {})

    suspend fun replaceAllWithBackStack(
        route: R,
        backStack: List<Transition<R>>,
        configuration: TransitionScope.() -> Unit = {},
    )

    suspend fun isInStack(route: R): Boolean

    fun addListener(listener: IStackListener<R>)

    fun removeListener(listener: IStackListener<R>)
}
