package io.github.yanadroidua.kroute.navigation.router.utils

import io.github.yanadroidua.kroute.navigation.router.IRouteExit
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

internal class StubRouteExit : IRouteExit {

    var onExitInvokeCount = 0
        private set

    val isInvoked: Boolean
        get() = onExitInvokeCount > 0

    val isNeverInvoked: Boolean
        get() = onExitInvokeCount == 0

    override suspend fun onExit(configuration: TransitionScope.() -> Unit) {
        onExitInvokeCount++
    }
}
