package io.github.yanadroidua.kroute.sample

import androidx.compose.ui.window.ComposeUIViewController
import io.github.yanadroidua.kroute.lint.utils.IgnoreNaming

@IgnoreNaming
fun MainViewController() = ComposeUIViewController(configure = { enforceStrictPlistSanityCheck = false }) {
    Application()
}
