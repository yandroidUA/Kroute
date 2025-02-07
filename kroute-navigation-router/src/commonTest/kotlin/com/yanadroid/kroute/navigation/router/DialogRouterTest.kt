package com.yanadroid.kroute.navigation.router

import com.yanadroid.kroute.navigation.router.utils.TestRoute
import com.yanadroid.kroute.navigation.router.utils.argumentsOf
import com.yanadroid.kroute.navigation.router.utils.testBooleanArgument
import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.test.utils.TestReceiveAndCollect
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DialogRouterTest {

    private interface DialogRouterTestScope {
        val router: DialogRouterImpl<TestRoute>
        val operationTestReceiveChannel: TestReceiveAndCollect<IOngoingOperation<DialogState<TestRoute>>>
        val currentTransition: TestReceiveAndCollect<Transition<DialogState<TestRoute>>>
    }

    private fun testDialogRouter(test: suspend DialogRouterTestScope.() -> Unit) = runTest {
        val router = DialogRouterImpl<TestRoute>()
        val operationTestReceiveChannel = TestReceiveAndCollect(
            channel = router.ongoingOperation,
            scope = this,
        )
        val currentRouteReceiveChannel = TestReceiveAndCollect(
            flow = router.currentTransition,
            scope = this,
        )
        currentRouteReceiveChannel.subscribe()
        operationTestReceiveChannel.subscribe()

        val testSuite = object : DialogRouterTestScope {
            override val router: DialogRouterImpl<TestRoute>
                get() = router
            override val operationTestReceiveChannel: TestReceiveAndCollect<IOngoingOperation<DialogState<TestRoute>>>
                get() = operationTestReceiveChannel
            override val currentTransition: TestReceiveAndCollect<Transition<DialogState<TestRoute>>>
                get() = currentRouteReceiveChannel
        }

        with(testSuite) { test() }

        operationTestReceiveChannel.ensureNoItemsAndClose()
        currentRouteReceiveChannel.ensureNoItemsAndClose()
    }

    @Test
    fun open_shouldSendForwardOngoingOperation_andUpdateCurrentTransition() = testDialogRouter {
        assertEquals(
            Transition<DialogState<TestRoute>>(destination = DialogState.Empty),
            currentTransition.pop(),
        )

        router.open(route = TestRoute.Main) {
            putArgument(testBooleanArgument, true)
        }

        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition<DialogState<TestRoute>>(
                destination = DialogState.Present(TestRoute.Main),
                arguments = argumentsOf(testBooleanArgument to true),
            ),
            transition.transition,
        )
        transition.start()
        assertEquals(
            Transition<DialogState<TestRoute>>(
                destination = DialogState.Present(TestRoute.Main),
                arguments = argumentsOf(testBooleanArgument to true),
            ),
            currentTransition.pop(),
        )
    }

    @Test
    fun close_shouldSendEmptyDialogState_andUpdateCurrentTransition() = testDialogRouter {
        assertEquals(
            Transition<DialogState<TestRoute>>(destination = DialogState.Empty),
            currentTransition.pop(),
        )

        router.open(route = TestRoute.Main)
        operationTestReceiveChannel.pop().start()
        currentTransition.pop()

        router.close()
        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition<DialogState<TestRoute>>(destination = DialogState.Empty),
            transition.transition,
        )
        transition.start()
        assertEquals(
            Transition<DialogState<TestRoute>>(destination = DialogState.Empty),
            currentTransition.pop(),
        )
    }
}
