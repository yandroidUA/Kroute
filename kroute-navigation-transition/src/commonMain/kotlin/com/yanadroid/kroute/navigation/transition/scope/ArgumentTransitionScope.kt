package com.yanadroid.kroute.navigation.transition.scope

import com.yanadroid.kroute.navigation.arguments.Argument
import com.yanadroid.kroute.navigation.transition.TransitionDslMarker

@TransitionDslMarker
interface ArgumentTransitionScope {

    fun <T : Any> putArgument(argument: Argument<T>, value: T)

    fun <T : Any> removeArgument(argument: Argument<T>)

    fun <T : Any> getArgumentOrNull(argument: Argument<T>): T?
}
