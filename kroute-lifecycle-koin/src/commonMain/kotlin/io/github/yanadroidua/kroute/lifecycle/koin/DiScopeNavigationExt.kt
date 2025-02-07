package io.github.yanadroidua.kroute.lifecycle.koin

import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import org.koin.core.Koin

/**
 * An extension function that can be used to install a [koin extension][DiScopeLifecycleStackListener] on the [router][IScreenRouter].
 *
 * @param diScopeRouteMapper maps route to the associated scope
 * @param koin an instance of the Koin
 */
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
