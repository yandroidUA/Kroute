package io.github.yanadroidua.kroute.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.yanadroidua.kroute.sample.di.KoinApplicationPlatformConfiguration
import io.github.yanadroidua.kroute.sample.di.initKoin

@Composable
private fun App() {
    Application()
}

fun main() = application {
    initKoin(platformConfiguration = KoinApplicationPlatformConfiguration())
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
