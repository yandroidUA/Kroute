package io.github.yanadroidua.kroute.lifecycle.viewmodel

import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory

/**
 * An extension function that can be used to install a [VM extension][ViewModelLifecycleStackListener] on the [router][IScreenRouter].
 *
 * @param factory creates a VM that is associated with the [route][R]
 * @param storage stores VMs instances
 * @param R route
 */
fun <R : Any> IScreenRouter<R>.installViewmodelExtension(
    factory: IViewModelFactory<R>,
    storage: ViewModelStorage,
): IScreenRouter<R> {
    val listener = ViewModelLifecycleStackListener(
        factory = factory,
        storage = storage,
    )

    addListener(listener = listener)

    return this
}
