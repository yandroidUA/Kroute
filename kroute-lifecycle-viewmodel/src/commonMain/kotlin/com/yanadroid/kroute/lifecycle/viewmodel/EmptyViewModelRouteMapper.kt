package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.viewmodel.IViewModel
import kotlin.reflect.KClass

/**
 * An empty [ViewModelRouteMapper] that always returns `null`.
 */
class EmptyViewModelRouteMapper<T : Any> : ViewModelRouteMapper<T> {

    override fun viewModelTypeOrNull(route: T): KClass<out IViewModel>? = null
}
