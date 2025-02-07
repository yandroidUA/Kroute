package io.github.yanadroidua.kroute.lifecycle.viewmodel

import io.github.yanadroidua.kroute.lifecycle.viewmodel.utils.TestDestination
import io.github.yanadroidua.kroute.lifecycle.viewmodel.utils.TestViewModel
import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestViewModelLifecycleListener {

    private val viewModelFactory: IViewModelFactory<TestDestination> = IViewModelFactory { type ->
        when (type) {
            TestDestination.ViewModelRoute -> TestViewModel()
            TestDestination.NoViewModelRoute -> null
        }
    }

    @Test
    fun onPopped_should_remove_viewmodel_from_storage() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        val key = ViewModelStorage.ViewModelKey(transactionId = "TEST", type = TestDestination.ViewModelRoute)
        storage.add(type = TestDestination.ViewModelRoute, transactionId = "TEST", viewModel = TestViewModel())

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
        assertNull(storage.storage[key.copy(transactionId = "")])
        assertNull(storage.storage[key.copy(transactionId = "TES")])

        listener.onPopped(
            popped = listOf(Transition(id = "TEST", destination = TestDestination.ViewModelRoute)),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isEmpty() }
        assertNull(storage.storage[key])
    }

    @Test
    fun onPopped_should_doNothing_if_route_no_associated_to_vm() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        storage.add(type = TestDestination.ViewModelRoute, transactionId = "TEST", viewModel = TestViewModel())
        val key = ViewModelStorage.ViewModelKey(transactionId = "TEST", type = TestDestination.ViewModelRoute)

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
        assertNull(storage.storage[key.copy(transactionId = "")])
        assertNull(storage.storage[key.copy(transactionId = "TES")])

        listener.onPopped(
            popped = listOf(Transition(id = "TEST", destination = TestDestination.NoViewModelRoute)),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
    }

    @Test
    fun onPopped_should_doNothing_if_id_not_match() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        storage.add(type = TestDestination.ViewModelRoute, transactionId = "TEST", viewModel = TestViewModel())
        val key = ViewModelStorage.ViewModelKey(transactionId = "TEST", type = TestDestination.ViewModelRoute)

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
        assertNull(storage.storage[key.copy(transactionId = "")])
        assertNull(storage.storage[key.copy(transactionId = "TES")])

        listener.onPopped(
            popped = listOf(Transition(id = "TES", destination = TestDestination.ViewModelRoute)),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
    }

    @Test
    fun onPushed_should_create_viewmodel_for_route_if_not_exists() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        assertTrue { storage.storage.isEmpty() }
        val key = ViewModelStorage.ViewModelKey(type = TestDestination.ViewModelRoute, transactionId = "TEST")

        listener.onPushed(
            transition = Transition(id = "TEST", destination = TestDestination.ViewModelRoute),
            stack = emptyList(),
        )

        assertFalse { storage.storage.isEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
    }

    @Test
    fun onPushed_should_create_viewmodel_for_route_if_not_exists_transaction_id_not_match() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        assertTrue { storage.storage.isEmpty() }

        listener.onPushed(
            transition = Transition(id = "TEST", destination = TestDestination.ViewModelRoute),
            stack = emptyList(),
        )

        val key1 = ViewModelStorage.ViewModelKey(type = TestDestination.ViewModelRoute, transactionId = "TEST")
        val key2 = ViewModelStorage.ViewModelKey(type = TestDestination.ViewModelRoute, transactionId = "TEST-NEW")

        assertFalse { storage.storage.isEmpty() }
        assertEquals(TestViewModel(), storage.storage[key1])

        listener.onPushed(
            transition = Transition(id = "TEST-NEW", destination = TestDestination.ViewModelRoute),
            stack = emptyList(),
        )

        assertFalse { storage.storage.isEmpty() }
        assertEquals(TestViewModel(), storage.storage[key1])
        assertEquals(TestViewModel(), storage.storage[key2])

        val vm1 = storage.storage[key1]
        val vm2 = storage.storage[key2]

        // asserting those are not same by comparing references
        assertFalse {
            vm1 === vm2
        }
    }

    @Test
    fun onPushed_should_NOT_create_viewmodel_for_route_if_exists() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        // adding a VM to a storage, simulating a situation where there was something in the storage
        val key = ViewModelStorage.ViewModelKey(transactionId = "TEST", type = TestDestination.ViewModelRoute)
        storage.add(type = TestDestination.ViewModelRoute, transactionId = "TEST", viewModel = TestViewModel())

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
        val existingViewModel = storage.getOrNull(transactionId = "TEST", type = TestDestination.ViewModelRoute)

        listener.onPushed(
            transition = Transition(id = "TEST", destination = TestDestination.ViewModelRoute),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isNotEmpty() }
        assertEquals(TestViewModel(), storage.storage[key])
        // compare by reference
        assertTrue {
            existingViewModel === storage.getOrNull(transactionId = "TEST", type = TestDestination.ViewModelRoute)
        }
    }

    @Test
    fun onPushed_should_NOT_create_viewmodel_for_route_if_no_viewmodel_associated() {
        val storage = ViewModelStorage()
        val listener = ViewModelLifecycleStackListener(
            factory = viewModelFactory,
            storage = storage,
        )

        assertTrue { storage.storage.isEmpty() }

        listener.onPushed(
            transition = Transition(id = "TEST", destination = TestDestination.NoViewModelRoute),
            stack = emptyList(),
        )

        assertTrue { storage.storage.isEmpty() }
    }
}
