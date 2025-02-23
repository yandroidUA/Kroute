package io.github.yanadroidua.kroute.navigation.ui

import io.github.yanadroidua.kroute.navigation.router.IRouteExit
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

class JvmApplicationExit : IRouteExit {
    override suspend fun onExit(configuration: TransitionScope.() -> Unit) = Unit
}
