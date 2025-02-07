package io.github.yanadroidua.kroute.navigation.router

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import io.github.yanadroidua.kroute.navigation.router.utils.StubRouteExit
import io.github.yanadroidua.kroute.navigation.router.utils.StubStackListener
import io.github.yanadroidua.kroute.navigation.router.utils.TestRoute
import io.github.yanadroidua.kroute.navigation.router.utils.argumentsOf
import io.github.yanadroidua.kroute.navigation.router.utils.testBooleanArgument
import io.github.yanadroidua.kroute.navigation.router.utils.testIntArgument
import io.github.yanadroidua.kroute.navigation.router.utils.testStringArgument
import io.github.yanadroidua.kroute.navigation.router.utils.transition
import io.github.yanadroidua.kroute.navigation.router.utils.withTestId
import io.github.yanadroidua.kroute.navigation.transition.Transition
import io.github.yanadroidua.kroute.test.utils.TestReceiveAndCollect
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
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )

        router.goTo(route = TestRoute.A)

        // verify stack has been changed
        var transition = operationTestReceiveChannel.pop()
        assertEquals(
            transition(TestRoute.A),
            transition.transition.withTestId(),
        )
        assertFalse { transition.isBack }
        assertEquals(
            listOf(
                transition(TestRoute.Main),
                transition(TestRoute.A),
            ),
            router.stack.withTestId(),
        )
        stackListener.verifyPushed(
            pushed = transition(destination = TestRoute.A),
            stack = listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A),
            ),
        )

        // verify that after ongoing transition finished the current transition
        transition.start()
        assertEquals(
            transition(destination = TestRoute.A),
            currentTransition.pop().withTestId(),
        )

        router.back {
            putArgument(testIntArgument, 100)
            currentExitAnimation(fadeOut())
        }
        transition = operationTestReceiveChannel.pop()
        assertEquals(
            transition(
                destination = TestRoute.Main,
                arguments = argumentsOf(testIntArgument to 100),
                currentExitAnimation = fadeOut(),
            ),
            transition.transition.withTestId(),
        )
        stackListener.verifyPopped(
            popped = listOf(
                transition(destination = TestRoute.A),
            ),
            stack = listOf(
                transition(
                    destination = TestRoute.Main,
                    arguments = argumentsOf(testIntArgument to 100),
                    currentExitAnimation = fadeOut(),
                ),
            ),
        )
        assertTrue { transition.isBack }
        assertEquals(
            listOf(
                transition(
                    destination = TestRoute.Main,
                    arguments = argumentsOf(testIntArgument to 100),
                    currentExitAnimation = fadeOut(),
                ),
            ),
            router.stack.withTestId(),
        )
        assertTrue { routeExit.isNeverInvoked }
        transition.start()
        assertEquals(
            transition(
                destination = TestRoute.Main,
                arguments = argumentsOf(testIntArgument to 100),
                currentExitAnimation = fadeOut(),
            ),
            currentTransition.pop().withTestId(),
        )
    }

    @Test
    fun back_shouldRemoveLastElementFromStack_andCall_routeExit() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )
        router.back()

        stackListener.verifyPopped(
            popped = listOf(transition(destination = TestRoute.Main)),
            stack = emptyList(),
        )

        assertTrue { routeExit.onExitInvokeCount == 1 }
        assertTrue { router.stack.withTestId().isEmpty() }
    }

    @Test
    fun isInStack_shouldReturnTrue_if_route_is_present() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )
        assertTrue { router.isInStack(route = TestRoute.Main) }
    }

    @Test
    fun isInStack_shouldReturnFalse_if_route_is_NOT_present() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )
        assertFalse { router.isInStack(route = TestRoute.A) }
    }

    @Test
    fun goTo_shouldChangeStack_andSend_ForwardOngoingOperation() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )

        router.goTo(TestRoute.A) {
            enterAnimation(fadeIn())
            currentExitAnimation(ExitTransition.None)
            putArgument(testBooleanArgument, true)
        }

        // verify stack has been changed
        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            transition(
                destination = TestRoute.A,
                enterAnimation = fadeIn(),
                arguments = mapOf(testBooleanArgument.storageKey to true),
            ),
            transition.transition.withTestId(),
        )
        assertFalse { transition.isBack }
        assertEquals(
            listOf(
                transition(destination = TestRoute.Main),
                transition(
                    destination = TestRoute.A,
                    enterAnimation = fadeIn(),
                    arguments = mapOf(testBooleanArgument.storageKey to true),
                ),
            ),
            router.stack.withTestId(),
        )
        // verify listeners have been notified
        stackListener.verifyPushed(
            pushed = transition(
                destination = TestRoute.A,
                enterAnimation = fadeIn(),
                arguments = mapOf(testBooleanArgument.storageKey to true),
            ),
            stack = listOf(
                transition(destination = TestRoute.Main),
                transition(
                    destination = TestRoute.A,
                    enterAnimation = fadeIn(),
                    arguments = mapOf(testBooleanArgument.storageKey to true),
                ),
            ),
        )
        assertTrue { routeExit.isNeverInvoked }
        transition.start()
        assertEquals(
            transition(
                destination = TestRoute.A,
                enterAnimation = fadeIn(),
                arguments = mapOf(testBooleanArgument.storageKey to true),
            ),
            currentTransition.pop().withTestId(),
        )
    }

    @Test
    fun replaceAll_removeOldValuesFromStack_addNew_andSend_ForwardOngoingOperation() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )

        router.goTo(TestRoute.A) {
            putArgument(testStringArgument, "test")
        }

        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            transition.transition.withTestId(),
        )
        assertEquals(
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            router.stack.withTestId(),
        )
        stackListener.verifyPushed(
            pushed = transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            stack = listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
        )
        transition.start()
        assertEquals(
            transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            currentTransition.pop().withTestId(),
        )

        // replace all stack
        router.replaceAll(TestRoute.B) {
            putArgument(testBooleanArgument, true)
        }
        val newTransition = operationTestReceiveChannel.pop()
        assertEquals(
            transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            newTransition.transition.withTestId(),
        )
        assertEquals(
            listOf(transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true))),
            router.stack.withTestId(),
        )
        stackListener.verifyPopped(
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            emptyList(),
        )
        stackListener.verifyPushed(
            pushed = transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            stack = listOf(
                transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            ),
        )
        newTransition.start()
        assertEquals(
            transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            currentTransition.pop().withTestId(),
        )

        assertTrue { routeExit.isNeverInvoked }
    }

    @Test
    fun popTo_shouldRemoveTopItemsUtilDesired_andSend_BackwardOngoingOperation() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )
        assertEquals(
            listOf(transition(destination = TestRoute.Main)),
            router.stack.withTestId(),
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
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A, arguments = argumentsOf(testIntArgument to 5)),
                transition(destination = TestRoute.B, arguments = argumentsOf(testStringArgument to "test")),
                transition(destination = TestRoute.C, arguments = argumentsOf(testBooleanArgument to true)),
            ),
            router.stack.withTestId(),
        )

        router.popTo(route = TestRoute.A) {
            putArgument(testBooleanArgument, false)
        }

        val transition = operationTestReceiveChannel.pop()
        assertEquals(
            transition(
                destination = TestRoute.A,
                arguments = argumentsOf(
                    testIntArgument to 5,
                    testBooleanArgument to false,
                ),
            ),
            transition.transition.withTestId(),
        )
        assertTrue { transition.isBack }

        assertEquals(
            listOf(
                transition(destination = TestRoute.Main),
                transition(
                    destination = TestRoute.A,
                    arguments = argumentsOf(
                        testIntArgument to 5,
                        testBooleanArgument to false,
                    ),
                ),
            ),
            router.stack.withTestId(),
        )

        stackListener.verifyPopped(
            listOf(
                transition(destination = TestRoute.B, arguments = argumentsOf(testStringArgument to "test")),
                transition(destination = TestRoute.C, arguments = argumentsOf(testBooleanArgument to true)),
            ),
            listOf(
                transition(destination = TestRoute.Main),
                transition(
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
            transition(
                destination = TestRoute.A,
                arguments = argumentsOf(
                    testIntArgument to 5,
                    testBooleanArgument to false,
                ),
            ),
            currentTransition.pop().withTestId(),
        )
    }

    @Test
    fun replaceAllWithBackStack_removeOldValuesFromStack_addNewValues_andSend_ForwardOngoingOperation_forLatest() =
        withRouterTest {
            assertEquals(
                transition(destination = TestRoute.Main),
                currentTransition.pop().withTestId(),
            )
            assertEquals(
                listOf(transition(destination = TestRoute.Main)),
                router.stack.withTestId(),
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
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                transition.transition.withTestId(),
            )
            assertEquals(
                listOf(
                    transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                    transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                ),
                router.stack.withTestId(),
            )
            stackListener.verifyPopped(
                listOf(transition(destination = TestRoute.Main)),
                emptyList(),
            )
            stackListener.verifyPushed(
                transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                listOf(transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true))),
            )
            stackListener.verifyPushed(
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                listOf(
                    transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                    transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                ),
            )
            transition.start()
            assertEquals(
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
                currentTransition.pop().withTestId(),
            )
        }

    @Test
    fun goToWithBackStack_shouldAddNewValues_andSend_ForwardOngoingOperation_forLatest() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )
        assertEquals(
            listOf(transition(destination = TestRoute.Main)),
            router.stack.withTestId(),
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
            transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            transition.transition.withTestId(),
        )
        assertEquals(
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
            router.stack.withTestId(),
        )
        stackListener.verifyPushed(
            transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
            ),
        )
        stackListener.verifyPushed(
            transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.B, arguments = argumentsOf(testBooleanArgument to true)),
                transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            ),
        )
        transition.start()
        assertEquals(
            transition(destination = TestRoute.A, arguments = argumentsOf(testStringArgument to "test")),
            currentTransition.pop().withTestId(),
        )
    }

    @Test
    fun onArgumentRemoved_shouldUpdateStack() = withRouterTest {
        assertEquals(
            transition(destination = TestRoute.Main),
            currentTransition.pop().withTestId(),
        )
        assertEquals(
            listOf(transition(destination = TestRoute.Main)),
            router.stack.withTestId(),
        )

        router.goTo(TestRoute.A) { putArgument(testIntArgument, 5) }
        operationTestReceiveChannel.pop()
        stackListener.skipPushed()

        assertEquals(
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A, arguments = argumentsOf(testIntArgument to 5)),
            ),
            router.stack.withTestId(),
        )

        val last = router.stack.last()

        router.onArgumentRemoved(
            last,
            testIntArgument,
        )

        assertEquals(
            listOf(
                transition(destination = TestRoute.Main),
                transition(destination = TestRoute.A),
            ),
            router.stack.withTestId(),
        )
    }
}
