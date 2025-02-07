package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.viewmodel.IViewModel
import kotlin.reflect.KClass

/**
 * A mapper that maps a route to a view model that its uses.
 */
fun interface ViewModelRouteMapper<R : Any> {

    /**
     * Maps a route to the view model type or `null` if a route doesn't use the view model.
     */
    fun viewModelTypeOrNull(route: R): KClass<out IViewModel>?
}
