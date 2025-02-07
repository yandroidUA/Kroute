package com.yanadroid.kroute.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.yanadroid.kroute.navigation.arguments.Argument
import com.yanadroid.kroute.navigation.arguments.ITransitionArguments

@Composable
fun <T : Any> ITransitionArguments.getNavArgumentOnceOrNull(argument: Argument<T>): T? = remember {
    getOrNull(argument)
}
