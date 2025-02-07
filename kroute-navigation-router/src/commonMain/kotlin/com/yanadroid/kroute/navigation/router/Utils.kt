package com.yanadroid.kroute.navigation.router

import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.navigation.transition.TransitionBuilder
import com.yanadroid.kroute.navigation.transition.scope.TransitionScope

fun <R : Any> transition(route: R, configuration: TransitionScope.() -> Unit = {}): Transition<R> =
    TransitionBuilder(route)
        .apply(configuration)
        .build()
