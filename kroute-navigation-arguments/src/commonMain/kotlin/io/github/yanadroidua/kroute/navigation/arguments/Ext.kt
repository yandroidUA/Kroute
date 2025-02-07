package io.github.yanadroidua.kroute.navigation.arguments

/**
 * Defines a new navigation arguments.
 * @param key argument's storage key
 * @param lifecycle argument's lifecycle.
 */
inline fun <reified T : Any> navArgument(
    key: String,
    lifecycle: ArgumentLifecycle = ArgumentLifecycle.PERMANENT,
): Argument<T> = Argument(
    type = T::class,
    storageKey = key,
    lifecycle = lifecycle,
)

/**
 * Defines a new one-time argument.
 * @param key argument's key
 */
inline fun <reified T : Any> oneTimeArgument(key: String): Argument<T> = navArgument(
    key = key,
    lifecycle = ArgumentLifecycle.ONE_TIME,
)
