package io.github.yanadroidua.kroute.lifecycle.koin

import io.github.aakira.napier.Napier
import io.github.yanadroidua.kroute.navigation.router.IStackListener
import io.github.yanadroidua.kroute.navigation.transition.Transition
import org.koin.core.Koin
import org.koin.core.component.getScopeId

/**
 * A navigation stack listener that automatically closes a Koin's scope once associated with the scope screen gets
 * removed from the navigation stack.
 *
 * @param diScopeRouteMapper maps route to the associated scope
 * @param koin an instance of the Koin
 */
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
