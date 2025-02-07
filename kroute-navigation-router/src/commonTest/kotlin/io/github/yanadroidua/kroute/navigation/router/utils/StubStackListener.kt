package io.github.yanadroidua.kroute.navigation.router.utils

import io.github.yanadroidua.kroute.navigation.router.IStackListener
import io.github.yanadroidua.kroute.navigation.transition.Transition
import kotlin.test.assertEquals

internal class StubStackListener : IStackListener<TestRoute> {

    val popped = mutableListOf<Pair<List<Transition<TestRoute>>, List<Transition<TestRoute>>>>()

    val pushed = mutableListOf<Pair<Transition<TestRoute>, List<Transition<TestRoute>>>>()

    override fun onPopped(popped: List<Transition<TestRoute>>, stack: List<Transition<TestRoute>>) {
        this.popped.add(popped to stack)
    }

    override fun onPushed(transition: Transition<TestRoute>, stack: List<Transition<TestRoute>>) {
        this.pushed.add(transition to stack)
    }

    fun verifyPopped(
        popped: List<Transition<TestRoute>>,
        stack: List<Transition<TestRoute>>,
    ) {
        val firstPopped = this.popped.removeAt(0)
        assertEquals(popped, firstPopped.first.withTestId())
        assertEquals(stack, firstPopped.second.withTestId())
    }

    fun verifyPushed(
        pushed: Transition<TestRoute>,
        stack: List<Transition<TestRoute>>,
    ) {
        val firstPushed = this.pushed.removeAt(0)
        assertEquals(pushed, firstPushed.first.withTestId())
        assertEquals(stack, firstPushed.second.withTestId())
    }

    fun skipPushed() {
        this.pushed.removeAt(0)
    }
}
