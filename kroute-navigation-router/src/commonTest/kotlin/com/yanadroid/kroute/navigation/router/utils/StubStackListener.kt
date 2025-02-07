package com.yanadroid.kroute.navigation.router.utils

import com.yanadroid.kroute.navigation.router.IStackListener
import com.yanadroid.kroute.navigation.transition.Transition
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
        assertEquals(popped, firstPopped.first)
        assertEquals(stack, firstPopped.second)
    }

    fun verifyPushed(
        pushed: Transition<TestRoute>,
        stack: List<Transition<TestRoute>>,
    ) {
        val firstPushed = this.pushed.removeAt(0)
        assertEquals(pushed, firstPushed.first)
        assertEquals(stack, firstPushed.second)
    }

    fun skipPushed() {
        this.pushed.removeAt(0)
    }
}
