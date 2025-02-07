package io.github.yanadroidua.kroute.navigation.arguments

/**
 * Reads arguments from the storage.
 */
interface ITransitionArguments {
    /**
     * @return an argument from the storage or `null` if argument has not been found in the storage
     */
    fun <T : Any> getOrNull(argument: Argument<T>): T?

    /**
     * @return `true` if argument exists in the storage, otherwise `false`
     */
    fun <T : Any> exists(argument: Argument<T>): Boolean
}
