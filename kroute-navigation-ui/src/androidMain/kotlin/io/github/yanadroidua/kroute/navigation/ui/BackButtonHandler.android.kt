package io.github.yanadroidua.kroute.navigation.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
actual fun RegisterBackButtonHandler(enabled: Boolean, onBack: () -> Unit) {
    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val currentOnBackPressed by rememberUpdatedState(newValue = { onBack() })

    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    LaunchedEffect(enabled) {
        backCallback.isEnabled = enabled
    }

    DisposableEffect(backPressDispatcher) {
        backPressDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}
