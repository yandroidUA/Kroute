package com.yanadroid.kroute.lifecycle.koin

import org.koin.core.scope.ScopeID

fun interface DiScopeRouteMapper<R : Any> {

    fun routeToScopeOrNull(route: R): ScopeID?
}
