package io.github.yanadroidua.kroute.ui.koin.viewmodel

import androidx.compose.runtime.Composable
import io.github.yanadroidua.kroute.lint.utils.AllowNoModifier
import io.github.yanadroidua.kroute.ui.koin.ScopedComposable
import io.github.yanadroidua.kroute.ui.viewmodel.ViewModelComposable
import io.github.yanadroidua.kroute.viewmodel.IViewModel
import org.koin.core.Koin
import org.koin.core.scope.Scope

/**
 * A combination of [ScopedComposable], [ViewModelComposable] and [rememberViewModel], that provides a flexibility to
 * create and remember a scoped viewmodel
 */
@Composable
@AllowNoModifier
inline fun <reified S : Any, reified VM : IViewModel> ScopedViewModelComposable(
    type: Any,
    koin: Koin,
    transitionId: String,
    crossinline content: @Composable (Scope, VM) -> Unit,
) = ScopedComposable<S>(koin = koin) { scope ->
    val viewModel = rememberViewModel<VM>(type = type, transitionId = transitionId, scope = scope)
    ViewModelComposable(
        viewModel = viewModel,
        composable = { content(scope, it) },
    )
}
