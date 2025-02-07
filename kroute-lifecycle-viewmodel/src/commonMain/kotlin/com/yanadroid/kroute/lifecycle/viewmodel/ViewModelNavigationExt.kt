package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.navigation.router.IScreenRouter
import com.yanadroid.kroute.viewmodel.IViewModelFactory

fun <R : Any> IScreenRouter<R>.installLifecycleExtension(
    routeMapper: ViewModelRouteMapper<R>,
    factory: IViewModelFactory,
    storage: ViewModelStorage = ViewModelStorage(),
): IScreenRouter<R> {
    val listener = ViewModelLifecycleStackListener(
        routeMapper = routeMapper,
        factory = factory,
        storage = storage,
    )

    addListener(listener = listener)

    return this
}
