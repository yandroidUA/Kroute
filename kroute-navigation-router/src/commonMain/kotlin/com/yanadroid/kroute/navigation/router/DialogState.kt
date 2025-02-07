package com.yanadroid.kroute.navigation.router

import androidx.compose.runtime.Immutable

@Immutable
sealed interface DialogState<out R : Any> {
    @Immutable
    data object Empty : DialogState<Nothing>

    @Immutable
    data class Present<R : Any>(val route: R) : DialogState<R>
}
