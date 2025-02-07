package io.github.yanadroidua.kroute.lifecycle.koin

import org.koin.core.scope.ScopeID

/**
 * Maps a route to the associated Koin scope.
 */
fun interface DiScopeRouteMapper<R : Any> {

    fun routeToScopeOrNull(route: R): ScopeID?
}
