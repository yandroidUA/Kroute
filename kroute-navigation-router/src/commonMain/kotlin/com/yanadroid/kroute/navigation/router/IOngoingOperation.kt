package com.yanadroid.kroute.navigation.router

import com.yanadroid.kroute.navigation.transition.Transition

interface IOngoingOperation<R : Any> {

    val transition: Transition<R>

    val isBack: Boolean

    suspend fun start()
}
