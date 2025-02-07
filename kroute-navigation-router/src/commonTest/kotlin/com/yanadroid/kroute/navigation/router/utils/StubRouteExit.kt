package com.yanadroid.kroute.navigation.router.utils

import com.yanadroid.kroute.navigation.router.IRouteExit

internal class StubRouteExit : IRouteExit {

    var onExitInvokeCount = 0
        private set

    val isInvoked: Boolean
        get() = onExitInvokeCount > 0

    val isNeverInvoked: Boolean
        get() = onExitInvokeCount == 0

    override suspend fun onExit() {
        onExitInvokeCount++
    }
}
