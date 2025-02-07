package io.github.yanadroidua.kroute.lifecycle.viewmodel

import io.github.aakira.napier.Napier
import io.github.yanadroidua.kroute.viewmodel.IViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModelProvider

/**
 * Stores VMs instances.
 */
class ViewModelStorage {

    internal data class ViewModelKey(
        val type: Any,
        val transactionId: String?,
    )

    internal val storage = HashMap<ViewModelKey, IViewModel>()

    /**
     * Saves a VM instance, associating it with the type and transactionId (if is not `null`).
     *
     * @param type type that VM should be associated with
     * @param viewModel instance of the VM
     * @param transactionId ID of the transaction or `null` if transaction should not be associated with the VM
     */
    fun add(type: Any, viewModel: IViewModel, transactionId: String? = null) {
        Napier.d { "Adding new VM ${viewModel::class.simpleName}, transaction: $transactionId" }

        val key = ViewModelKey(type = type, transactionId = transactionId)
        val oldVm = storage[key]

        if (oldVm != null) {
            Napier.d { "Automatically replacing old VM ${oldVm::class.simpleName}" }
            oldVm.onDestroy()
        }

        storage[key] = viewModel
    }

    /**
     * Removes a VM instance associated with a particular type and/or transaction ID.
     * @param type type that VM should be associated with
     * @param transactionId ID of the transaction or `null` if transaction should not be associated with the VM
     */
    fun remove(
        type: Any,
        transactionId: String? = null,
    ) {
        val key = ViewModelKey(type = type, transactionId = transactionId)
        val viewModel = storage.remove(key)

        if (viewModel != null) {
            Napier.i { "VM $type has been removed transaction: $transactionId" }
            viewModel.onDestroy()
        } else {
            Napier.w { "VM $type is not present in the VM storage, transaction: $transactionId" }
        }
    }

    /**
     * Searches for the [viewmodel][IViewModel] instance associated with particular type and/or transaction ID, if no
     * associated [viewmodel][IViewModel] with passed arguments or type is wrong `null` will be returned
     *
     * @param type type that VM should be associated with
     * @param transactionId ID of the transaction or `null` if transaction should not be associated with the VM
     * @return an instance of the [IViewModel] associated with particular type and/or transaction ID or `null`
     */
    fun getOrNull(type: Any, transactionId: String? = null): IViewModel? {
        val key = ViewModelKey(type = type, transactionId = transactionId)
        return storage[key]?.also {
            Napier.d { "Returning $type from storage for transaction $transactionId" }
        } ?: run {
            Napier.d { "Cannot find $type in the VM storage associated with $transactionId" }
            null
        }
    }

    /**
     * Removes all stored instances from the storage.
     */
    fun clear() {
        Napier.d { "Clearing VM storage, contained ${storage.keys} VMs" }
        storage.clear()
    }
}

/**
 * Searches for the [viewmodel][IViewModel] instance  associated with particular type and/or transaction ID and casts it
 * to the requested [viewmodel type][T], if no associated [viewmodel][IViewModel] with passed arguments or type is wrong `null` will be returned
 *
 * @param type type that VM should be associated with
 * @param T a particular type of the [viewmodel][IViewModel]
 * @param transactionId ID of the transaction or `null` if transaction should not be associated with the VM
 * @return an instance of [T] associated with particular type and/or transaction ID or `null`
 */
inline fun <reified T : IViewModel> ViewModelStorage.getOrNull(type: Any, transactionId: String? = null): T? {
    val viewModel = getOrNull(transactionId = transactionId, type = type) ?: return null

    if (viewModel !is T) {
        Napier.e { "Failed to cast ${viewModel::class.simpleName} to the ${T::class.simpleName}" }
        return null
    }

    return viewModel
}

/**
 * Searches for the [viewmodel][IViewModel] instance  associated with particular type and/or transaction ID and casts it
 * to the requested [viewmodel type][T], if no associated [viewmodel][IViewModel] with passed arguments or type is wrong
 * then it uses a passed factory function to create one and store it in the storage.
 *
 * @param type type that VM should be associated with
 * @param T a particular type of the [viewmodel][IViewModel]
 * @param transactionId ID of the transaction or `null` if transaction should not be associated with the VM
 * @param factory a factory to create a new instance of the [T]
 * @return an instance of [T] associated with particular type and/or transaction ID
 * @see [ViewModelStorage.add]
 */
inline fun <reified T : IViewModel> ViewModelStorage.getOrCreate(
    type: Any,
    factory: (String?) -> T,
    transactionId: String? = null,
): T = getOrNull<T>(type = type, transactionId = transactionId) ?: run {
    Napier.d { "Creating a new instance of ${T::class.simpleName} from the factory" }
    factory(transactionId).also { add(type, it, transactionId) }
}

/**
 * Searches for the [viewmodel][IViewModel] instance  associated with particular type and/or transaction ID and casts it
 * to the requested [viewmodel type][T], if no associated [viewmodel][IViewModel] with passed arguments or type is wrong
 * then it uses a passed viewmodel provider to create one and store it in the storage.
 *
 * @param type type that VM should be associated with
 * @param T a particular type of the [viewmodel][IViewModel]
 * @param transactionId ID of the transaction or `null` if transaction should not be associated with the VM
 * @param provider a viewmodel provider [T]
 * @return an instance of [T] associated with particular type and/or transaction ID
 * @see getOrCreate
 */
inline fun <reified T : IViewModel> ViewModelStorage.getOrCreate(
    type: Any,
    provider: IViewModelProvider<T>,
    transactionId: String? = null,
): T = getOrCreate(type = type, transactionId = transactionId, factory = { provider.create() })
