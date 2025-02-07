package io.github.yanadroidua.kroute.viewmodel

/**
 * Creates an instance of the particular viewmodel.
 */
fun interface IViewModelProvider<T : IViewModel> {

    fun create(): T
}
