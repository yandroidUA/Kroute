package com.yanadroid.kroute.viewmodel

import kotlin.reflect.KClass

fun interface IViewModelFactory {

    fun viewModel(type: KClass<out IViewModel>): IViewModel?
}
