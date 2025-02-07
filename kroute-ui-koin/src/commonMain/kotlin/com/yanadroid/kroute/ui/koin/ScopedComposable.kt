package com.yanadroid.kroute.ui.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.yanadroid.kroute.lint.utils.AllowNoModifier
import org.koin.core.Koin
import org.koin.core.scope.Scope

@Composable
@AllowNoModifier
inline fun <reified S : Any> ScopedComposable(
    koin: Koin,
    content: @Composable (Scope) -> Unit,
) {
    val scope = remember {
        koin.getOrCreateScope<S>(
            scopeId = requireNotNull(S::class.simpleName) { "Cannot get the name of the scope from the class name" },
        )
    }

    content(scope)
}
