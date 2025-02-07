package com.yanadroid.kroute.lifecycle.viewmodel

import com.yanadroid.kroute.lifecycle.viewmodel.utils.TestViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestViewModelStorage {

    @Test
    fun add_should_add_new_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(viewModel)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.storage[TestViewModel::class])
    }

    @Test
    fun add_should_call_onDestroy_for_old_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()
        assertFalse { viewModel.isAboutToBeDestroyed }

        storage.add(viewModel)

        assertEquals(viewModel, storage.storage[TestViewModel::class])

        // new VM
        val newViewModel = TestViewModel()

        assertFalse { storage.storage.isEmpty() }
        assertFalse { newViewModel.isAboutToBeDestroyed }

        storage.add(newViewModel)

        assertEquals(newViewModel, storage.storage[TestViewModel::class])
        assertTrue { viewModel.isAboutToBeDestroyed }
    }

    @Test
    fun remove_should_call_onDestroy_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()
        assertFalse { viewModel.isAboutToBeDestroyed }

        storage.add(viewModel)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.storage[TestViewModel::class])

        storage.remove(TestViewModel::class)
        assertTrue { viewModel.isAboutToBeDestroyed }
        assertTrue { storage.storage.isEmpty() }
    }

    @Test
    fun get_should_return_viewmodel() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(viewModel)

        assertFalse { storage.storage.isEmpty() }
        assertEquals(viewModel, storage.getOrNull(TestViewModel::class))
    }

    @Test
    fun clear_should_remove_all_viewmodels() {
        val storage = ViewModelStorage()

        assertTrue { storage.storage.isEmpty() }
        val viewModel = TestViewModel()

        storage.add(viewModel)

        assertFalse { storage.storage.isEmpty() }

        storage.clear()
        assertTrue { storage.storage.isEmpty() }
    }
}
