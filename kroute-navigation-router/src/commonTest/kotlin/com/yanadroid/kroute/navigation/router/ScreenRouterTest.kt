package com.yanadroid.kroute.navigation.router

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.yanadroid.kroute.navigation.router.utils.StubRouteExit
import com.yanadroid.kroute.navigation.router.utils.StubStackListener
import com.yanadroid.kroute.navigation.router.utils.TestRoute
import com.yanadroid.kroute.navigation.router.utils.argumentsOf
import com.yanadroid.kroute.navigation.router.utils.testBooleanArgument
import com.yanadroid.kroute.navigation.router.utils.testIntArgument
import com.yanadroid.kroute.navigation.router.utils.testStringArgument
import com.yanadroid.kroute.navigation.transition.Transition
import com.yanadroid.kroute.test.utils.TestReceiveAndCollect
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScreenRouterTest {

    private interface RouterTestSuit {
        val routeExit: StubRouteExit
        val router: ScreenRouterImpl<TestRoute>
        val stackListener: StubStackListener
        val operationTestReceiveChannel: TestReceiveAndCollect<IOngoingOperation<TestRoute>>
        val currentTransition: TestReceiveAndCollect<Transition<TestRoute>>
    }

    private fun withRouterTest(test: suspend RouterTestSuit.() -> Unit) = runTest {
        val routeExit = StubRouteExit()
        val router = ScreenRouterImpl(initialDestination = TestRoute.Main, routeExit = routeExit)
        val stackListener = StubStackListener()
        router.addListener(stackListener)
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

        val testSuite = object : RouterTestSuit {
            override val routeExit: StubRouteExit
                get() = routeExit
            override val router: ScreenRouterImpl<TestRoute>
                get() = router
            override val stackListener: StubStackListener
                get() = stackListener
            override val operationTestReceiveChannel: TestReceiveAndCollect<IOngoingOperation<TestRoute>>
                get() = operationTestReceiveChannel
            override val currentTransition: TestReceiveAndCollect<Transition<TestRoute>>
                get() = currentRouteReceiveChannel
        }

        with(testSuite) { test() }

        operationTestReceiveChannel.ensureNoItemsAndClose()
        currentRouteReceiveChannel.ensureNoItemsAndClose()
    }

    @Test
    fun back_shouldRemoveLastElementFromStack_andSend_BackwardOngoingOperation() = withRouterTest {
        // verify the initial route
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )

        router.goTo(route = TestRoute.A)

        // verify stack has been changed
        var transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(TestRoute.A),
            transition.transition,
        )
        assertFalse { transition.isBack }
        assertEquals(
            listOf(
                Transition(TestRoute.Main),
                Transition(TestRoute.A),
            ),
            router.stack,
        )
        stackListener.verifyPushed(
            pushed = Transition(destination = TestRoute.A),
            stack = listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A),
            ),
        )

        // verify that after ongoing transition finished the current transition
        transition.start()
        assertEquals(
            Transition(destination = TestRoute.A),
            currentTransition.pop(),
        )

        router.back {
            putArgument(testIntArgument, 100)
            currentExitAnimation(fadeOut())
        }
        transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(
                destination = TestRoute.Main,
                arguments = argumentsOf(testIntArgument to 100),
                currentExitAnimation = fadeOut(),
            ),
            transition.transition,
        )
        stackListener.verifyPopped(
            popped = listOf(
                Transition(destination = TestRoute.A),
            ),
            stack = listOf(
                Transition(
                    destination = TestRoute.Main,
                    arguments = argumentsOf(testIntArgument to 100),
                    currentExitAnimation = fadeOut(),
                ),
            ),
        )
        assertTrue { transition.isBack }
        assertEquals(
            listOf(
                Transition(
                    destination = TestRoute.Main,
                    arguments = argumentsOf(testIntArgument to 100),
                    currentExitAnimation = fadeOut(),
                ),
            ),
            router.stack,
        )
        assertTrue { routeExit.isNeverInvoked }
        transition.start()
        assertEquals(
            Transition(
                destination = TestRoute.Main,
                arguments = argumentsOf(testIntArgument to 100),
                currentExitAnimation = fadeOut(),
            ),
            currentTransition.pop(),
        )
    }

    @Test
    fun back_shouldRemoveLastElementFromStack_andCall_routeExit() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        router.back()

        stackListener.verifyPopped(
            popped = listOf(Transition(destination = TestRoute.Main)),
            stack = emptyList(),
        )

        assertTrue { routeExit.onExitInvokeCount == 1 }
        assertTrue { router.stack.isEmpty() }
    }

    @Test
    fun isInStack_shouldReturnTrue_if_route_is_present() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        assertTrue { router.isInStack(route = TestRoute.Main) }
    }

    @Test
    fun isInStack_shouldReturnFalse_if_route_is_NOT_present() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        assertFalse { router.isInStack(route = TestRoute.A) }
    }

    @Test
    fun goTo_shouldChangeStack_andSend_ForwardOngoingOperation() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )

        router.goTo(TestRoute.A) {
            enterAnimation(fadeIn())
            currentExitAnimation(ExitTransition.None)
            putArgument(testBooleanArgument, true)
        }

        // verify stack has been changed
        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(
                destination = TestRoute.A,
                enterAnimation = fadeIn(),
                arguments = mapOf(testBooleanArgument.storageKey to true),
            ),
            transition.transition,
        )
        assertFalse { transition.isBack }
        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(
                    destination = TestRoute.A,
                    enterAnimation = fadeIn(),
                    arguments = mapOf(testBooleanArgument.storageKey to true),
                ),
            ),
            router.stack,
        )
        // verify listeners have been notified
        stackListener.verifyPushed(
            pushed = Transition(
                destination = TestRoute.A,
                enterAnimation = fadeIn(),
                arguments = mapOf(testBooleanArgument.storageKey to true),
            ),
            stack = listOf(
                Transition(destination = TestRoute.Main),
                Transition(
                    destination = TestRoute.A,
                    enterAnimation = fadeIn(),
                    arguments = mapOf(testBooleanArgument.storageKey to true),
                ),
            ),
        )
        assertTrue { routeExit.isNeverInvoked }
        transition.start()
        assertEquals(
            Transition(
                destination = TestRoute.A,
                enterAnimation = fadeIn(),
                arguments = mapOf(testBooleanArgument.storageKey to true),
            ),
            currentTransition.pop(),
        )
    }

    @Test
    fun replaceAll_removeOldValuesFromStack_addNew_andSend_ForwardOngoingOperation() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )

        router.goTo(TestRoute.A) {
            putArgument(testStringArgument, "test")
        }

        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            transition.transition,
        )
        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            router.stack,
        )
        stackListener.verifyPushed(
            pushed = Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            stack = listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
        )
        transition.start()
        assertEquals(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            currentTransition.pop(),
        )

        // replace all stack
        router.replaceAll(TestRoute.B) {
            putArgument(testBooleanArgument, true)
        }
        val newTransition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            newTransition.transition,
        )
        assertEquals(
            listOf(Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true))),
            router.stack,
        )
        stackListener.verifyPopped(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            emptyList(),
        )
        stackListener.verifyPushed(
            pushed = Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            stack = listOf(
                Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            ),
        )
        newTransition.start()
        assertEquals(
            Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            currentTransition.pop(),
        )

        assertTrue { routeExit.isNeverInvoked }
    }

    @Test
    fun popTo_shouldClearStack_andCall_routeExit_ifNoRouteFound() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )

        router.goTo(TestRoute.A) {
            putArgument(testStringArgument, "test")
        }

        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            transition.transition,
        )
        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            router.stack,
        )
        stackListener.verifyPushed(
            pushed = Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            stack = listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
        )
        transition.start()
        assertEquals(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            currentTransition.pop(),
        )

        router.popTo(route = TestRoute.B)
        stackListener.verifyPopped(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            emptyList(),
        )
        assertEquals(emptyList(), router.stack)

        assertTrue { routeExit.isInvoked }
        assertTrue { routeExit.onExitInvokeCount == 1 }
    }

    @Test
    fun popTo_shouldClearStack_andCall_routeExit_ifRouteIsRoot() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        assertEquals(
            listOf(Transition(destination = TestRoute.Main)),
            router.stack,
        )

        router.popTo(route = TestRoute.Main)
        stackListener.verifyPopped(
            listOf(Transition(destination = TestRoute.Main)),
            emptyList(),
        )
        assertEquals(emptyList(), router.stack)

        assertTrue { routeExit.isInvoked }
        assertTrue { routeExit.onExitInvokeCount == 1 }
    }

    @Test
    fun popTo_shouldRemoveTopItemsUtilDesired_andSend_BackwardOngoingOperation() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        assertEquals(
            listOf(Transition(destination = TestRoute.Main)),
            router.stack,
        )

        router.goTo(TestRoute.A) { putArgument(testIntArgument, 5) }
        operationTestReceiveChannel.pop()
        stackListener.skipPushed()

        router.goTo(TestRoute.B) { putArgument(testStringArgument, "test") }
        operationTestReceiveChannel.pop()
        stackListener.skipPushed()

        router.goTo(TestRoute.C) { putArgument(testBooleanArgument, true) }
        operationTestReceiveChannel.pop()
        stackListener.skipPushed()

        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testIntArgument to 5)),
                Transition(destination = TestRoute.B, arguments = argumentsOf(testStringArgument to "test")),
                Transition(destination = TestRoute.C, arguments = argumentsOf(testBooleanArgument to true)),
            ),
            router.stack,
        )

        router.popTo(route = TestRoute.A) {
            putArgument(testBooleanArgument, false)
        }

        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            Transition(
                destination = TestRoute.A,
                arguments = argumentsOf(
                    testIntArgument to 5,
                    testBooleanArgument to false,
                ),
            ),
            transition.transition,
        )
        assertTrue { transition.isBack }

        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(
                    destination = TestRoute.A,
                    arguments = argumentsOf(
                        testIntArgument to 5,
                        testBooleanArgument to false,
                    ),
                ),
            ),
            router.stack,
        )

        stackListener.verifyPopped(
            listOf(
                Transition(destination = TestRoute.B, arguments = argumentsOf(testStringArgument to "test")),
                Transition(destination = TestRoute.C, arguments = argumentsOf(testBooleanArgument to true)),
            ),
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(
                    destination = TestRoute.A,
                    arguments = argumentsOf(
                        testIntArgument to 5,
                        testBooleanArgument to false,
                    ),
                ),
            ),
        )

        transition.start()
        assertEquals(
            Transition(
                destination = TestRoute.A,
                arguments = argumentsOf(
                    testIntArgument to 5,
                    testBooleanArgument to false,
                ),
            ),
            currentTransition.pop(),
        )
    }

    @Test
    fun replaceAllWithBackStack_removeOldValuesFromStack_addNewValues_andSend_ForwardOngoingOperation_forLatest() =
        withRouterTest {
            assertEquals(
                Transition(destination = TestRoute.Main),
                currentTransition.pop(),
            )
            assertEquals(
                listOf(Transition(destination = TestRoute.Main)),
                router.stack,
            )

            router.replaceAllWithBackStack(
                route = TestRoute.A,
                backStack = listOf(
                    transition(TestRoute.B) {
                        putArgument(testBooleanArgument, true)
                    },
                ),
            ) {
                putArgument(testStringArgument, "test")
            }

            val transition = operationTestReceiveChannel.pop()
            assertFalse { transition.isBack }
            assertEquals(
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                transition.transition,
            )
            assertEquals(
                listOf(
                    Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                    Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                ),
                router.stack,
            )
            stackListener.verifyPopped(
                listOf(Transition(destination = TestRoute.Main)),
                emptyList(),
            )
            stackListener.verifyPushed(
                Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                listOf(Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true))),
            )
            stackListener.verifyPushed(
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                listOf(
                    Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                    Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                ),
            )
            transition.start()
            assertEquals(
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                currentTransition.pop(),
            )
        }

    @Test
    fun goToWithBackStack_shouldAddNewValues_andSend_ForwardOngoingOperation_forLatest() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        assertEquals(
            listOf(Transition(destination = TestRoute.Main)),
            router.stack,
        )

        router.goToWithBackStack(
            route = TestRoute.A,
            backStack = listOf(
                transition(TestRoute.B) {
                    putArgument(testBooleanArgument, true)
                },
            ),
        ) {
            putArgument(testStringArgument, "test")
        }

        val transition = operationTestReceiveChannel.pop()
        assertFalse { transition.isBack }
        assertEquals(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            transition.transition,
        )
        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            router.stack,
        )
        stackListener.verifyPushed(
            Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            ),
        )
        stackListener.verifyPushed(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
        )
        transition.start()
        assertEquals(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            currentTransition.pop(),
        )
    }

    @Test
    fun onArgumentRemoved_shouldUpdateStack() = withRouterTest {
        assertEquals(
            Transition(destination = TestRoute.Main),
            currentTransition.pop(),
        )
        assertEquals(
            listOf(Transition(destination = TestRoute.Main)),
            router.stack,
        )

        router.goTo(TestRoute.A) { putArgument(testIntArgument, 5) }
        operationTestReceiveChannel.pop()
        stackListener.skipPushed()

        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A, arguments = argumentsOf(testIntArgument to 5)),
            ),
            router.stack,
        )

        router.onArgumentRemoved(
            Transition(destination = TestRoute.A, arguments = argumentsOf(testIntArgument to 5)),
            testIntArgument,
        )

        assertEquals(
            listOf(
                Transition(destination = TestRoute.Main),
                Transition(destination = TestRoute.A),
            ),
            router.stack,
        )
    }
}
