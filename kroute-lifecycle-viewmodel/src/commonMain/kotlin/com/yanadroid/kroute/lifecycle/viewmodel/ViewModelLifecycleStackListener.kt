package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.navigation.router.IStackListener
import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.viewmodel.IViewModelFactory
import io.github.aakira.napier.Napier

/**
 * Removes/adds VMs once the associate routes have been pushed/popped from the navigation stack.
 * @param routeMapper maps the route to the VM class
 * @param factory creates an instance of the VM based on its class
 * @param storage persists the VM instance
 */
internal class ViewModelLifecycleStackListener<R : Any>(
    private val routeMapper: ViewModelRouteMapper<R>,
    private val factory: IViewModelFactory,
    private val storage: ViewModelStorage,
) : IStackListener<R> {

    override fun onPopped(popped: List<Transition<R>>, stack: List<Transition<R>>) {
        super.onPopped(popped, stack)
        popped
            .mapNotNull { routeMapper.viewModelTypeOrNull(route = it.destination) }
            .onEach(storage::remove)
    }

    override fun onPushed(transition: Transition<R>, stack: List<Transition<R>>) {
        super.onPushed(transition, stack)

        val viewModelClass = routeMapper.viewModelTypeOrNull(transition.destination)

        if (viewModelClass != null) {
            var viewModel = storage.getOrNull(viewModelClass)

            if (viewModel == null) {
                Napier.d { "VM $viewModelClass is not yet present in the storage, creating a new instance" }

                viewModel = factory.viewModel(viewModelClass)

                if (viewModel != null) {
                    storage.add(viewModel)
                } else {
                    Napier.w { "Failed to instantiate a VM from the factory for the $viewModelClass" }
                }
            } else {
                Napier.d { "VM $viewModelClass is already present in the storage" }
            }
        } else {
            Napier.w { "Cannot find a view model for the ${transition.destination}" }
        }
    }
}
