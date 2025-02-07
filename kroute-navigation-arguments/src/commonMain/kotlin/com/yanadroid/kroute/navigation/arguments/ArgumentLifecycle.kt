package com.yanadroid.kroute.navigation.arguments

/**
 * Represents a lifecycle of the argument.
 */
enum class ArgumentLifecycle {
    /**
     * It means that argument should be consumed only once and removed immediately after consumption.
     */
    ONE_TIME,

    /**
     * It means that argument should persist and should not be removed after the consumption.
     */
    PERMANENT,
}
