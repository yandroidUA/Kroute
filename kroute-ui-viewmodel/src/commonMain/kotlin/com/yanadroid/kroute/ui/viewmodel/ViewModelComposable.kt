package com.yanadroid.kroute.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.yanadroid.kroute.lint.utils.AllowNoModifier
import com.yanadroid.kroute.viewmodel.IViewModel

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
