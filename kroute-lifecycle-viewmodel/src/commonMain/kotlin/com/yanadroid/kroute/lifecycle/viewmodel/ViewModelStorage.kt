package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.viewmodel.IViewModel
import com.yanadroid.kroute.viewmodel.IViewModelProvider
import io.github.aakira.napier.Napier
import kotlin.reflect.KClass

class ViewModelStorage {

    internal val storage = HashMap<KClass<out IViewModel>, IViewModel>()

    fun add(viewModel: IViewModel) {
        Napier.d { "Adding new VM ${viewModel::class.simpleName}" }

        val oldVm = storage[viewModel::class]

        if (oldVm != null) {
            Napier.d { "Automatically replacing old VM ${oldVm::class.simpleName}" }
            oldVm.onDestroy()
        }

        storage[viewModel::class] = viewModel
    }

    fun remove(type: KClass<out IViewModel>) {
        val viewModel = storage.remove(type)

        if (viewModel != null) {
            Napier.i { "VM ${type.simpleName} has been removed" }
            viewModel.onDestroy()
        } else {
            Napier.w { "VM ${type.simpleName} is not present in the VM storage" }
        }
    }

    fun getOrNull(type: KClass<out IViewModel>): IViewModel? = storage[type]?.also {
        Napier.d { "Returning $type from storage" }
    } ?: run {
        Napier.d { "Cannot find $type in the VM storage" }
        null
    }

    fun clear() {
        Napier.d { "Clearing VM storage, contained ${storage.keys} VMs" }
        storage.clear()
    }
}

inline fun <reified T : IViewModel> ViewModelStorage.getOrNull(): T? {
    val viewModel = getOrNull(type = T::class) ?: return null

    if (viewModel !is T) {
        Napier.e { "Failed to cast ${viewModel::class.simpleName} to the ${T::class.simpleName}" }
        return null
    }

    return viewModel
}

inline fun <reified T : IViewModel> ViewModelStorage.getOrCreate(factory: () -> T): T = getOrNull<T>()
    ?: run {
        Napier.d { "Creating a new instance of ${T::class.simpleName} from the factory" }
        factory().also { add(it) }
    }

inline fun <reified T : IViewModel> ViewModelStorage.getOrCreate(provider: IViewModelProvider<T>): T = getOrCreate {
    provider.create()
}
