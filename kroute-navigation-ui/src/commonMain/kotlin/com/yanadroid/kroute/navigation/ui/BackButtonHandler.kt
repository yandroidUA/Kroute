package com.yanadroid.kroute.navigation.ui

import androidx.compose.runtime.Composable

@Composable
expect fun RegisterBackButtonHandler(enabled: Boolean, onBack: () -> Unit)
