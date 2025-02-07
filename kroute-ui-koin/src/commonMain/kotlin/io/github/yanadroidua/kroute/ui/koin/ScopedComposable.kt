package io.github.yanadroidua.kroute.ui.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.yanadroidua.kroute.lifecycle.koin.scopeId
import io.github.yanadroidua.kroute.lint.utils.AllowNoModifier
import org.koin.core.Koin
import org.koin.core.scope.Scope

/**
 * A composable that **gets or creates** a requested Koin's scope and passes it down to the children composable.
 *
 * @param koin koin application
 * @see content children composable that uses the Koin's scope
 * @see Koin
 * @see Scope
 */
@Composable
@AllowNoModifier
inline fun <reified S : Any> ScopedComposable(
    koin: Koin,
    content: @Composable (Scope) -> Unit,
) {
    val scope = remember {
        koin.getOrCreateScope<S>(
            scopeId = scopeId<S>(),
        )
    }

    content(scope)
}
