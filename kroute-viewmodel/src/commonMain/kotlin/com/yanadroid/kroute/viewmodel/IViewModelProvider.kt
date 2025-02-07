package com.yanadroid.kroute.viewmodel

fun interface IViewModelProvider<T : IViewModel> {

    fun create(): T
}
