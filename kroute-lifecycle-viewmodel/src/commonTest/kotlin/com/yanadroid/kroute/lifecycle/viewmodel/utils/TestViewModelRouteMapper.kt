package com.yanadroid.kroute.lifecycle.viewmodel.utils

import com.yanadroid.kroute.lifecycle.viewmodel.ViewModelRouteMapper
import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestDestination.NoViewModelRoute
import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestDestination.ViewModelRoute
import com.yanadroid.kroute.viewmodel.IViewModel
import kotlin.reflect.KClass

internal class TestViewModelRouteMapper : ViewModelRouteMapper<TestDestination> {
    override fun viewModelTypeOrNull(route: TestDestination): KClass<out IViewModel>? = when (route) {
        ViewModelRoute -> TestViewModel::class
        NoViewModelRoute -> null
    }
}
