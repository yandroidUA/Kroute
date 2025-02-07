package io.github.yanadroidua.kroute.viewmodel

/**
 * Provides an instance of the [IViewModel] for the [route][R] or `null`.
 */
fun interface IViewModelFactory<R : Any> {

    fun viewModel(route: R): IViewModel?
}
