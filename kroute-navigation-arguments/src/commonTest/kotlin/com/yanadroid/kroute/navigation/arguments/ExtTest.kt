package com.yanadroid.kroute.navigation.arguments

import kotlin.test.Test
import kotlin.test.assertEquals

class ExtTest {

    @Test
    fun navArgument_shouldCreatedArgumentWithPERMANENTLifecycle_byDefault() {
        val argument = navArgument<String>(key = "my-key")

        assertEquals("my-key", argument.storageKey)
        assertEquals(String::class, argument.type)
        assertEquals(ArgumentLifecycle.PERMANENT, argument.lifecycle)
    }

    @Test
    fun navArgument_shouldBeAbleToChangeLifecycle() {
        val argument = navArgument<Boolean>(key = "my-key-new", lifecycle = ArgumentLifecycle.ONE_TIME)

        assertEquals("my-key-new", argument.storageKey)
        assertEquals(Boolean::class, argument.type)
        assertEquals(ArgumentLifecycle.ONE_TIME, argument.lifecycle)
    }

    @Test
    fun oneTimeArgument_shouldAlwaysHave_ONE_TIME_lifecycle() {
        val argument = oneTimeArgument<Int>(key = "my-key-one-time")

        assertEquals("my-key-one-time", argument.storageKey)
        assertEquals(Int::class, argument.type)
        assertEquals(ArgumentLifecycle.ONE_TIME, argument.lifecycle)
    }
}
