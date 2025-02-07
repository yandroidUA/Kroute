package com.yanadroid.kroute.lifecycle.koin

import com.yanadroid.kroute.lifecycle.koin.TestDiScopeLifecycleStackListener.TestRoute.WithScope
import com.yanadroid.kroute.lifecycle.koin.TestDiScopeLifecycleStackListener.TestRoute.WithoutScope
import com.yanadroid.kroute.navigation.transition.Transition
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestDiScopeLifecycleStackListener : KoinTest {

    private enum class TestRoute {
        WithScope,
        WithoutScope,
    }

    private interface TestScope

    private val diScopeRouteMapper: DiScopeRouteMapper<TestRoute> = DiScopeRouteMapper<TestRoute> { route ->
        when (route) {
            WithScope -> TestScope::class.simpleName
            WithoutScope -> null
        }
    }

    @BeforeTest
    fun initialize() {
        startKoin { }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_not_do_anything_if_no_scope_associated_in_onPopped() {
        val koin = getKoin()

        val scope = koin.createScope<TestScope>(scopeId = TestScope::class.simpleName!!)

        // verifying it is active
        assertFalse { scope.closed }

        val diScopeLifecycleStackListener = DiScopeLifecycleStackListener(
            diScopeRouteMapper = diScopeRouteMapper,
            koin = koin,
        )

        diScopeLifecycleStackListener.onPopped(
            popped = listOf(Transition(destination = WithoutScope)),
            stack = emptyList(),
        )

        // still active since it's not accosted with the mentioned route
        assertFalse { scope.closed }
    }

    @Test
    fun should_not_do_anything_if_no_scope_in_onPopped() {
        val koin = getKoin()

        assertNull(koin.getScopeOrNull(TestScope::class.simpleName!!))

        val diScopeLifecycleStackListener = DiScopeLifecycleStackListener(
            diScopeRouteMapper = diScopeRouteMapper,
            koin = koin,
        )

        diScopeLifecycleStackListener.onPopped(
            popped = listOf(Transition(destination = WithScope)),
            stack = emptyList(),
        )

        assertNull(koin.getScopeOrNull(TestScope::class.simpleName!!))
    }

    @Test
    fun should_close_scope_if_onPopped() {
        val koin = getKoin()

        val scope = koin.createScope<TestScope>(scopeId = TestScope::class.simpleName!!)

        assertFalse { scope.closed }

        val diScopeLifecycleStackListener = DiScopeLifecycleStackListener(
            diScopeRouteMapper = diScopeRouteMapper,
            koin = koin,
        )

        diScopeLifecycleStackListener.onPopped(
            popped = listOf(Transition(destination = WithScope)),
            stack = emptyList(),
        )

        assertTrue { scope.closed }
    }
}
