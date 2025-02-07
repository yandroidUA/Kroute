package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestDestination
import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestViewModel
import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestViewModelRouteMapper
import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.viewmodel.IViewModelFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestViewModelLifecycleListener {

    private val viewModelFactory: IViewModelFactory = IViewModelFactory { type ->
        when (type) {
            TestViewModel::class -> TestViewModel()
            else -> null
        }
    }

    @Test
    fun onPopped_should_remove_viewmodel_from_storage() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            routeMapper = TestViewModelRouteMapper(),
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        storage.add(TestViewModel())

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[TestViewModel::class])

        listener.onPopped(
            popped = listOf(Transition(destination = TestDestination.ViewModelRoute)),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isEmpty() }
        assertNull(storage.storage[TestViewModel::class])
    }

    @Test
    fun onPopped_should_doNothing_if_no_association_storage() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            routeMapper = TestViewModelRouteMapper(),
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        storage.add(TestViewModel())

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[TestViewModel::class])

        listener.onPopped(
            popped = listOf(Transition(destination = TestDestination.NoViewModelRoute)),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[TestViewModel::class])
    }

    @Test
    fun onPushed_should_create_viewmodel_for_route_if_not_exists() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            routeMapper = TestViewModelRouteMapper(),
            factory = viewModelFactory,
            storage = storage,
        )

        assertTrue { storage.storage.isEmpty() }

        listener.onPushed(
            transition = Transition(destination = TestDestination.ViewModelRoute),
            stack = emptyList(),
        )

        assertFalse { storage.storage.isEmpty() }
        assertEquals(TestViewModel(), storage.storage[TestViewModel::class])
    }

    @Test
    fun onPushed_should_NOT_create_viewmodel_for_route_if_exists() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            routeMapper = TestViewModelRouteMapper(),
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        storage.add(TestViewModel())

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[TestViewModel::class])
        val existingViewModel = storage.getOrNull(TestViewModel::class)

        listener.onPushed(
            transition = Transition(destination = TestDestination.ViewModelRoute),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[TestViewModel::class])
        // compare by reference
        assertTrue {
            existingViewModel === storage.getOrNull(TestViewModel::class)
        }
    }

    @Test
    fun onPushed_should_NOT_create_viewmodel_for_route_if_no_viewmodel_associated() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            routeMapper = TestViewModelRouteMapper(),
            factory = viewModelFactory,
            storage = storage,
        )

        assertTrue { storage.storage.isEmpty() }

        listener.onPushed(
            transition = Transition(destination = TestDestination.NoViewModelRoute),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isEmpty() }
    }
}
