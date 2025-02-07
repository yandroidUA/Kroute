package com.yanadroid.kroute.navigation.router.utils

import com.yanadroid.kroute.navigation.arguments.Argument
import com.yanadroid.kroute.navigation.arguments.navArgument

internal val testBooleanArgument = navArgument<Boolean>(key = "test-boolean")
internal val testIntArgument = navArgument<Int>(key = "test-int")
internal val testStringArgument = navArgument<String>(key = "test-string")

internal fun argumentsOf(vararg args: Pair<Argument<*>, Any>): Map<String, Any> = mapOf(
    *args.map { it.first.storageKey to it.second }.toTypedArray(),
)
