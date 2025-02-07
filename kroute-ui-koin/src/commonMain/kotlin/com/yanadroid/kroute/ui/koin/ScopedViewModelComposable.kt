package com.yanadroid.kroute.ui.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.yanadroid.kroute.lint.utils.AllowNoModifier
import com.yanadroid.kroute.ui.viewmodel.ViewModelComposable
import com.yanadroid.kroute.viewmodel.IViewModel
import org.koin.core.Koin

@Composable
@AllowNoModifier
inline fun <reified S : Any, reified VM : IViewModel> ScopedViewModelComposable(
    koin: Koin,
    crossinline content: @Composable (VM) -> Unit,
) = ScopedComposable<S>(koin = koin) { scope ->
    val viewModel = remember { scope.get<VM>() }
    ViewModelComposable(
        viewModel = viewModel,
        composable = { content(it) },
    )
}
