package io.github.yanadroidua.kroute.ui.navigation.koin.viewmodel

import androidx.compose.runtime.Composable
import io.github.yanadroidua.kroute.navigation.ui.NavigationViewScope
import io.github.yanadroidua.kroute.viewmodel.IViewModel
import org.koin.core.Koin
import org.koin.core.scope.Scope

/**
 * Remembers viewmodel utilizing [NavigationViewScope]
 */
@Composable
inline fun <R : Any, reified VM : IViewModel> NavigationViewScope<R>.rememberViewModel(koin: Koin): VM = io.github.yanadroidua.kroute.ui.koin.viewmodel.rememberViewModel(
    type = type,
    transitionId = transitionId,
    koin = koin,
)

/**
 * Remembers scoped viewmodel utilizing [NavigationViewScope]
 */
@Composable
inline fun <R : Any, reified VM : IViewModel> NavigationViewScope<R>.rememberViewModel(scope: Scope): VM = io.github.yanadroidua.kroute.ui.koin.viewmodel.rememberViewModel(
    type = type,
    transitionId = transitionId,
    scope = scope,
)
