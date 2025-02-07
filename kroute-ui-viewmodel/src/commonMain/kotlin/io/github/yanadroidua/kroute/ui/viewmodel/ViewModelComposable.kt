package io.github.yanadroidua.kroute.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import io.github.yanadroidua.kroute.lint.utils.AllowNoModifier
import io.github.yanadroidua.kroute.viewmodel.IViewModel

/**
 * A viewmodel composable that automatically activates viewmodel once composable added to the tree and pauses it on
 * pause when composable removes the tree.
 *
 * @param viewModel viewmodel
 * @param composable a composable that gets activated viewmodel
 */
@Composable
@AllowNoModifier
fun <VM : IViewModel> ViewModelComposable(
    viewModel: VM,
    composable: @Composable (VM) -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.onStart()
    }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.onPause()
        }
    }

    composable(viewModel)
}
