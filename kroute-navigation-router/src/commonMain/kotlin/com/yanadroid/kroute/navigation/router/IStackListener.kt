package com.yanadroid.kroute.navigation.router

import com.yanadroid.kroute.navigation.transition.Transition

interface IStackListener<R : Any> {

    fun onPushed(transition: Transition<R>, stack: List<Transition<R>>) {}

    fun onPopped(popped: List<Transition<R>>, stack: List<Transition<R>>) {}
}
