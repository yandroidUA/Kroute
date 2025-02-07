package com.yanadroid.kroute.lifecycle.koin

import com.yanadroid.kroute.navigation.router.IScreenRouter
import org.koin.core.Koin

fun <R : Any> IScreenRouter<R>.installDiScopeExtension(
    diScopeRouteMapper: DiScopeRouteMapper<R>,
    koin: Koin,
): IScreenRouter<R> {
    val listener = DiScopeLifecycleStackListener(
        diScopeRouteMapper = diScopeRouteMapper,
        koin = koin,
    )

    addListener(listener = listener)

    return this
}
