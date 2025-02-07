package io.github.yanadroidua.kroute.navigation.router

import io.github.yanadroidua.kroute.navigation.router.utils.TestRoute
import io.github.yanadroidua.kroute.navigation.router.utils.argumentsOf
import io.github.yanadroidua.kroute.navigation.router.utils.testBooleanArgument
import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.test.utils.TestReceiveAndCollect
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
            DialogState.Empty,
            currentTransition.pop().destination,
        )

        router.open(route = TestRoute.Main) {
            putArgument(testBooleanArgument, true)
        }

        var transition = operationTestReceiveChannel.pop()
        assertEquals(
            DialogState.Present(TestRoute.Main),
            transition.transition.destination,
        )
        assertEquals(
            argumentsOf(testBooleanArgument to true),
            transition.transition.arguments,
        )

        transition.start()

        val t = currentTransition.pop()
        assertEquals(
            DialogState.Present(TestRoute.Main),
            t.destination,
        )
        assertEquals(
            argumentsOf(testBooleanArgument to true),
            t.arguments,
        )
    }

    @Test
    fun close_shouldSendEmptyDialogState_andUpdateCurrentTransition() = testDialogRouter {
        assertEquals(
            DialogState.Empty,
            currentTransition.pop().destination,
        )

        router.open(route = TestRoute.Main)
        operationTestReceiveChannel.pop().start()
        currentTransition.pop()

        router.close()
        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            DialogState.Empty,
            transition.transition.destination,
        )
        transition.start()
        assertEquals(
            DialogState.Empty,
            currentTransition.pop().destination,
        )
    }
}
