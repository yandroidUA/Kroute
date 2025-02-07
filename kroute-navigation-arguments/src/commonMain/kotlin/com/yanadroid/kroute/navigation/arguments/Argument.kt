package com.yanadroid.kroute.navigation.arguments

import kotlin.reflect.KClass

/**
 * @param type type of the argument.
 * @param lifecycle lifecycle of the argument
 * @param storageKey key of the argument
 */
data class Argument<T : Any>(
    val type: KClass<T>,
    val lifecycle: ArgumentLifecycle,
    val storageKey: String,
)
