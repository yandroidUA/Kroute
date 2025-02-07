package com.yanadroid.kroute.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestCoroutineViewModel {

    private class StubTestCoroutineViewModel(dispatcher: TestDispatcher) : CoroutineViewModel(dispatcher) {

        val scope: CoroutineScope?
            get() = viewModelScope

        fun publicLaunchJob(block: suspend () -> Unit): Job? = launchJob(block)

        fun <T> publicAsync(block: suspend () -> T): Deferred<T>? = async(block)

        fun publicLaunch(block: suspend () -> Unit) = launch(block)
    }

    private fun startViewModel(testScheduler: TestCoroutineScheduler): StubTestCoroutineViewModel {
        val viewModel = StubTestCoroutineViewModel(dispatcher = StandardTestDispatcher(testScheduler))

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }

        viewModel.onStart()

        val viewModelScope = viewModel.scope
        assertNotNull(viewModelScope)
        assertTrue { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }
        assertTrue { viewModelScope.isActive }

        return viewModel
    }

    @Test
    fun onStart_should_create_scope() = runTest {
        startViewModel(testScheduler)
    }

    @Test
    fun onPause_should_terminate_job() = runTest {
        val viewModel = startViewModel(testScheduler)

        viewModel.onPause()
        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertTrue { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }
    }

    @Test
    fun onDestroy_should_terminate_job() = runTest {
        val viewModel = startViewModel(testScheduler)

        viewModel.onDestroy()
        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertTrue { viewModel.isAboutToBeDestroyed }
    }

    @Test
    fun launch_should_execute_a_coroutine() = runTest {
        val viewModel = startViewModel(testScheduler)
        var flag = false

        viewModel.publicLaunch {
            delay(100)
            flag = true
            delay(100)
        }
        testScheduler.advanceUntilIdle()

        assertTrue { flag }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun launch_should_be_stopped_once_viewmodel_paused() = runTest {
        val viewModel = startViewModel(testScheduler)
        var flag = false
        var increment = 0

        viewModel.publicLaunch {
            delay(50)
            flag = true
            delay(50)
            increment++
        }
        testScheduler.advanceTimeBy(51)
        viewModel.onPause()

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertTrue { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }

        assertTrue { flag }
        // verifying increment wasn't invoked because operation was cancelled
        assertEquals(0, increment)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun launch_should_be_stopped_once_viewmodel_destroyed() = runTest {
        val viewModel = startViewModel(testScheduler)
        var flag = false
        var increment = 0

        viewModel.publicLaunch {
            delay(50)
            flag = true
            delay(50)
            increment++
        }
        testScheduler.advanceTimeBy(51)
        viewModel.onDestroy()

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertTrue { viewModel.isAboutToBeDestroyed }

        assertTrue { flag }
        // verifying increment wasn't invoked because operation was cancelled
        assertEquals(0, increment)
    }

    @Test
    fun launch_should_not_execute_if_scope_not_started() = runTest {
        val viewModel = StubTestCoroutineViewModel(dispatcher = StandardTestDispatcher(testScheduler))
        var flag = false

        assertNull(viewModel.scope)
        assertFalse { flag }
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }

        viewModel.publicLaunch {
            flag = true
        }

        testScheduler.advanceUntilIdle()

        assertFalse { flag }

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }
    }

    @Test
    fun launchJob_should_execute_a_coroutine() = runTest {
        val viewModel = startViewModel(testScheduler)
        var flag = false

        val job = viewModel.publicLaunchJob {
            delay(100)
            flag = true
            delay(100)
        }
        assertNotNull(job)
        assertTrue { job.isActive }
        assertFalse { job.isCancelled }
        assertFalse { job.isCompleted }

        testScheduler.advanceUntilIdle()

        job.join()
        assertFalse { job.isActive }
        assertFalse { job.isCancelled }
        assertTrue { job.isCompleted }

        assertTrue { flag }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun launchJob_should_be_stopped_once_viewmodel_paused() = runTest {
        val viewModel = startViewModel(testScheduler)
        var flag = false
        var increment = 0

        val job = viewModel.publicLaunchJob {
            delay(50)
            flag = true
            delay(50)
            increment++
        }

        assertNotNull(job)
        assertTrue { job.isActive }
        assertFalse { job.isCancelled }
        assertFalse { job.isCompleted }

        testScheduler.advanceTimeBy(51)

        viewModel.onPause()

        job.join()

        assertFalse { job.isActive }
        assertTrue { job.isCancelled }
        assertTrue { job.isCompleted }

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertTrue { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }

        assertTrue { flag }
        // verifying increment wasn't invoked because operation was cancelled
        assertEquals(0, increment)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun launchJob_should_be_stopped_once_viewmodel_destroyed() = runTest {
        val viewModel = startViewModel(testScheduler)
        var flag = false
        var increment = 0

        val job = viewModel.publicLaunchJob {
            delay(50)
            flag = true
            delay(50)
            increment++
        }
        assertNotNull(job)
        assertTrue { job.isActive }
        assertFalse { job.isCancelled }
        assertFalse { job.isCompleted }

        testScheduler.advanceTimeBy(51)

        viewModel.onDestroy()

        job.join()

        assertFalse { job.isActive }
        assertTrue { job.isCancelled }
        assertTrue { job.isCompleted }

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertTrue { viewModel.isAboutToBeDestroyed }

        assertTrue { flag }
        // verifying increment wasn't invoked because operation was cancelled
        assertEquals(0, increment)
    }

    @Test
    fun launchJob_should_return_null_if_scope_not_started() = runTest {
        val viewModel = StubTestCoroutineViewModel(dispatcher = StandardTestDispatcher(testScheduler))
        var flag = false

        assertNull(viewModel.scope)
        assertFalse { flag }
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }

        val job = viewModel.publicLaunchJob {
            flag = true
        }

        testScheduler.advanceUntilIdle()

        assertNull(job)
        assertFalse { flag }

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun async_should_execute_a_coroutine() = runTest {
        val viewModel = startViewModel(testScheduler)

        val value1 = viewModel.publicAsync {
            delay(20)
            1
        }
        val value2 = viewModel.publicAsync {
            delay(35)
            2
        }

        assertNotNull(value1)
        assertTrue { value1.isActive }
        assertFalse { value1.isCancelled }
        assertFalse { value1.isCompleted }

        assertNotNull(value2)
        assertTrue { value2.isActive }
        assertFalse { value2.isCancelled }
        assertFalse { value2.isCompleted }

        val timeStart = testScheduler.currentTime
        val result = value1.await() + value2.await()
        val endTime = testScheduler.currentTime

        // max value of 2 delays
        assertEquals(35, endTime - timeStart)

        assertFalse { value1.isActive }
        assertFalse { value1.isCancelled }
        assertTrue { value1.isCompleted }

        assertFalse { value2.isActive }
        assertFalse { value2.isCancelled }
        assertTrue { value2.isCompleted }

        assertEquals(3, result)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun async_should_be_stopped_once_viewmodel_paused() = runTest {
        val viewModel = startViewModel(testScheduler)

        val value1 = viewModel.publicAsync {
            delay(20)
            1
        }
        val value2 = viewModel.publicAsync {
            delay(35)
            2
        }

        assertNotNull(value1)
        assertTrue { value1.isActive }
        assertFalse { value1.isCancelled }
        assertFalse { value1.isCompleted }

        assertNotNull(value2)
        assertTrue { value2.isActive }
        assertFalse { value2.isCancelled }
        assertFalse { value2.isCompleted }

        testScheduler.advanceTimeBy(21)
        viewModel.onPause()

        assertFalse { value1.isActive }
        assertFalse { value1.isCancelled }
        assertTrue { value1.isCompleted }

        assertFalse { value2.isActive }
        assertTrue { value2.isCancelled }
        assertFalse { value2.isCompleted }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun async_should_be_stopped_once_viewmodel_destroyed() = runTest {
        val viewModel = startViewModel(testScheduler)

        val value1 = viewModel.publicAsync {
            delay(20)
            1
        }
        val value2 = viewModel.publicAsync {
            delay(35)
            2
        }

        assertNotNull(value1)
        assertTrue { value1.isActive }
        assertFalse { value1.isCancelled }
        assertFalse { value1.isCompleted }

        assertNotNull(value2)
        assertTrue { value2.isActive }
        assertFalse { value2.isCancelled }
        assertFalse { value2.isCompleted }

        testScheduler.advanceTimeBy(21)
        viewModel.onDestroy()

        assertFalse { value1.isActive }
        assertFalse { value1.isCancelled }
        assertTrue { value1.isCompleted }

        assertFalse { value2.isActive }
        assertTrue { value2.isCancelled }
        assertFalse { value2.isCompleted }
    }

    @Test
    fun async_should_return_null_if_scope_not_started() = runTest {
        val viewModel = StubTestCoroutineViewModel(dispatcher = StandardTestDispatcher(testScheduler))

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }

        val value1 = viewModel.publicAsync {
            delay(20)
            1
        }
        val value2 = viewModel.publicAsync {
            delay(35)
            2
        }

        testScheduler.advanceUntilIdle()

        assertNull(value1)
        assertNull(value2)

        assertNull(viewModel.scope)
        assertFalse { viewModel.isActive }
        assertFalse { viewModel.isPaused }
        assertFalse { viewModel.isAboutToBeDestroyed }
    }
}
