package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestDestination
import kotlin.test.Test
import kotlin.test.assertNull

class TestEmptyViewModelRouteMapper {

    @Test
    fun viewModelTypeOrNull_returnNull_for_ViewModelRoute() {
        val mapper = EmptyViewModelRouteMapper<TestDestination>()

        assertNull(mapper.viewModelTypeOrNull(route = TestDestination.ViewModelRoute))
    }

    @Test
    fun viewModelTypeOrNull_returnNull_for_NoViewModelRoute() {
        val mapper = EmptyViewModelRouteMapper<TestDestination>()

        assertNull(mapper.viewModelTypeOrNull(route = TestDestination.NoViewModelRoute))
    }
}
