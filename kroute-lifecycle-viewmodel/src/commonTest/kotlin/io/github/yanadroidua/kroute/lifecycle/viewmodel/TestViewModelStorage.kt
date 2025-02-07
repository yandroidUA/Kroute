package io.github.yanadroidua.kroute.lifecycle.viewmodel

import io.github.yanadroidua.kroute.lifecycle.viewmodel.utils.TestDestination
import io.github.yanadroidua.kroute.lifecycle.viewmodel.utils.TestViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestViewModelStorage {

    private val key = ViewModelStorage.ViewModelKey(type = TestDestination.ViewModelRoute, transactionId = "TEST")
    private val key2 = ViewModelStorage.ViewModelKey(type = TestDestination.ViewModelRoute, transactionId = "TEST-2")

    @Test
    fun add_should_add_new_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(key.type, viewModel, key.transactionId)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.storage[key])
    }

    @Test
    fun add_should_add_new_viewmodel_with_new_id() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(key.type, viewModel, key.transactionId)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.storage[key])

        val viewModel2 = TestViewModel()

        storage.add(key2.type, viewModel2, key2.transactionId)
        assertEquals(viewModel2, storage.storage[key2])

        assertFalse { viewModel.isAboutToBeDestroyed }
        assertFalse { viewModel2.isAboutToBeDestroyed }

        assertFalse {
            storage.storage[key] === storage.storage[key2]
        }
    }

    @Test
    fun add_should_call_onDestroy_for_old_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()
        assertFalse { viewModel.isAboutToBeDestroyed }

        storage.add(key.type, viewModel, key.transactionId)

        assertEquals(viewModel, storage.storage[key])

        // new VM
        val newViewModel = TestViewModel()

        assertFalse { storage.storage.isEmpty() }
        assertFalse { newViewModel.isAboutToBeDestroyed }

        storage.add(key.type, newViewModel, key.transactionId)

        assertEquals(newViewModel, storage.storage[key])
        assertTrue { viewModel.isAboutToBeDestroyed }
    }

    @Test
    fun remove_should_call_onDestroy_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()
        assertFalse { viewModel.isAboutToBeDestroyed }

        storage.add(key.type, viewModel, key.transactionId)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.storage[key])

        storage.remove(key.type, key.transactionId)
        assertTrue { viewModel.isAboutToBeDestroyed }
        assertTrue { storage.storage.isEmpty() }
    }

    @Test
    fun get_should_return_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(key.type, viewModel, key.transactionId)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.getOrNull(key.type, key.transactionId))
    }

    @Test
    fun clear_should_remove_all_viewmodels() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(key.type, viewModel, key.transactionId)

        assertFalse { storage.storage.isEmpty() }

        storage.clear()
        assertTrue { storage.storage.isEmpty() }
    }
}
