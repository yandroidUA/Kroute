package com.yanadroid.kroute.lifecycle.koin

import com.yanadroid.kroute.navigation.router.IStackListener
import com.yanadroid.kroute.navigation.transition.Transition
import io.github.aakira.napier.Napier
import org.koin.core.Koin
import org.koin.core.component.getScopeId

internal class DiScopeLifecycleStackListener<R : Any>(
    private val diScopeRouteMapper: DiScopeRouteMapper<R>,
    private val koin: Koin,
) : IStackListener<R> {

    override fun onPopped(popped: List<Transition<R>>, stack: List<Transition<R>>) {
        super.onPopped(popped, stack)
        popped
            .mapNotNull { diScopeRouteMapper.routeToScopeOrNull(it.destination) }
            .mapNotNull { koin.getScopeOrNull(it) }
            .onEach { scope ->
                if (scope.closed) {
                    Napier.d { "Scope ${scope.getScopeId()} is already closed" }
                } else {
                    Napier.d { "Scope ${scope.getScopeId()} has been closed" }
                    scope.close()
                }
            }
    }
}
