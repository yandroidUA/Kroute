package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.runtime.Composable

/**
 * Handles a physical back button.
 *
 * @param enabled whether handler is enabled or disabled
 * @param onBack callback to handle back button
 */
@Composable
expect fun RegisterBackButtonHandler(enabled: Boolean, onBack: () -> Unit)
