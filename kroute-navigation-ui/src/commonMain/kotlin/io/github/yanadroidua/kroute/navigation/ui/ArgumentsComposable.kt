package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.yanadroidua.kroute.navigation.arguments.Argument
import io.github.yanadroidua.kroute.navigation.arguments.ITransitionArguments

/**
 * Combines [remember] and [getOrNull] functions into a single composable, to get and remember the transition's argument.
 *
 * @param argument argument to get from transition
 */
@Composable
fun <T : Any> ITransitionArguments.rememberArgumentOrNull(argument: Argument<T>): T? = remember {
    getOrNull(argument)
}
