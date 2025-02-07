package com.yanadroid.kroute.navigation.router

import com.yanadroid.kroute.navigation.arguments.Argument
import com.yanadroid.kroute.navigation.transition.Transition

interface IArgumentHolder<R : Any> {

    fun <T : Any> onArgumentRemoved(transition: Transition<R>, argument: Argument<T>)
}
