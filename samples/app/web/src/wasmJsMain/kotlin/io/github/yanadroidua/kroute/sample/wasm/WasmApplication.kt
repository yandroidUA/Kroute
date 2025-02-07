package io.github.yanadroidua.kroute.sample.wasm

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.yanadroidua.kroute.sample.Application
import io.github.yanadroidua.kroute.sample.di.KoinApplicationPlatformConfiguration
import io.github.yanadroidua.kroute.sample.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(platformConfiguration = KoinApplicationPlatformConfiguration())
    CanvasBasedWindow(title = "Kroute", canvasElementId = "krouteCanvas") {
        Application()
    }
}
