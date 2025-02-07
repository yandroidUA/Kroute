package io.github.yanadroidua.kroute.koin.viewmodel

import io.github.yanadroidua.kroute.viewmodel.IViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModelProvider
import org.koin.core.Koin
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL

/**
 * A utility function that generates a qualifier for a viewmodel provider.
 * It uses the class of viewmodel and adding `_provider` to it in order to compose a unique for a viewmodel key.
 * @param VM type of the viewmodel
 */
inline fun <reified VM : IViewModel> namedProvider(): StringQualifier = named(name = VM::class.simpleName!! + "_provider")

/**
 * Defines a viewmodel provider inside the Koin's [module][Module].
 * @param VM type of the viewmodel
 */
inline fun <reified VM : IViewModel> Module.viewModelProvider(noinline provider: Scope.() -> IViewModelProvider<VM>): KoinDefinition<IViewModelProvider<VM>> = factory(namedProvider<VM>()) {
    with(provider) { invoke(this@factory) }
}

/**
 * Defines a viewmodel provider inside the Koin's [scope][Scope].
 * @param VM type of the viewmodel
 */
inline fun <reified VM : IViewModel> ScopeDSL.viewModelProvider(noinline provider: Scope.() -> IViewModelProvider<VM>) {
    factory(namedProvider<VM>()) {
        with(provider) { invoke(this@factory) }
    }
}

/**
 * Retrieves a viewmodel [provider][IViewModelProvider] from a given [scope][Scope].
 * @param VM type of the viewmodel
 * @return a [provider][IViewModelProvider] of the viewmodel from a given [scope][Scope]
 */
inline fun <reified VM : IViewModel> Scope.getViewModelProvider(): IViewModelProvider<VM> = get<IViewModelProvider<VM>>(namedProvider<VM>())

/**
 * Retrieves a viewmodel [provider][IViewModelProvider] from a [koin][Koin].
 * @param VM type of the viewmodel
 * @return a [provider][IViewModelProvider] of the viewmodel from a [koin][Koin]
 */
inline fun <reified VM : IViewModel> Koin.getViewModelProvider(): IViewModelProvider<VM> = get<IViewModelProvider<VM>>(namedProvider<VM>())
