package io.github.yanadroidua.kroute.lifecycle.viewmodel

import io.github.aakira.napier.Napier
import io.github.yanadroidua.kroute.navigation.router.IStackListener
import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory

/**
 * Removes/adds VMs once the associate routes have been pushed/popped from the navigation stack.
 * @param factory creates an instance of the VM based on its class
 * @param storage persists the VM instances
 */
internal class ViewModelLifecycleStackListener<R : Any>(
    private val factory: IViewModelFactory<R>,
    private val storage: ViewModelStorage,
) : IStackListener<R> {

    override fun onPopped(popped: List<Transition<R>>, stack: List<Transition<R>>) {
        super.onPopped(popped, stack)
        popped
            .onEach { transition ->
                storage.remove(type = transition.destination, transactionId = transition.id)
            }
    }

    override fun onPushed(transition: Transition<R>, stack: List<Transition<R>>) {
        super.onPushed(transition, stack)

        var viewModel = storage.getOrNull(transactionId = transition.id, type = transition.destination)

        if (viewModel == null) {
            Napier.d { "VM ${transition.destination} (transaction: ${transition.id}) is not yet present in the storage, creating a new instance" }

            viewModel = factory.viewModel(transition.destination)

            if (viewModel != null) {
                storage.add(type = transition.destination, transactionId = transition.id, viewModel = viewModel)
            } else {
                Napier.w { "Failed to instantiate a VM from the factory for the ${transition.destination}" }
            }
        } else {
            Napier.d { "VM ${transition.destination} is already present in the storage" }
        }
    }
}
