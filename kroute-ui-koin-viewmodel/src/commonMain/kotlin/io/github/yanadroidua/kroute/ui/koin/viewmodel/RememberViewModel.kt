package io.github.yanadroidua.kroute.ui.koin.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.yanadroidua.kroute.koin.viewmodel.getViewModelProvider
import io.github.yanadroidua.kroute.lifecycle.viewmodel.ViewModelStorage
import io.github.yanadroidua.kroute.lifecycle.viewmodel.getOrCreate
import io.github.yanadroidua.kroute.lint.utils.AllowNoModifier
import io.github.yanadroidua.kroute.viewmodel.IViewModel
import org.koin.core.Koin
import org.koin.core.scope.Scope

/**
 * Remembers a viewmodel by utilizing Koin to either get it from the storage or create one and put it into the storage.
 *
 * @param type type key (usually it's a route/destination which is tied to the viewmodel)
 * @param transitionId ID of the transition
 * @param koin koin application
 *
 * @see ViewModelStorage
 * @see [ViewModelStorage.getOrCreate]
 */
@Composable
@AllowNoModifier
inline fun <reified VM : IViewModel> rememberViewModel(
    type: Any,
    transitionId: String,
    koin: Koin,
): VM = remember {
    val storage: ViewModelStorage = koin.get()
    storage.getOrCreate<VM>(
        type = type,
        transactionId = transitionId,
        provider = koin.getViewModelProvider<VM>(),
    )
}

/**
 * Remembers a viewmodel by utilizing Koin to either get it from the storage or create one and put it into the storage.
 *
 * @param type type key (usually it's a route/destination which is tied to the viewmodel)
 * @param transitionId ID of the transition
 * @param scope scope to look a viewmodel in
 *
 * @see ViewModelStorage
 * @see [ViewModelStorage.getOrCreate]
 */
@Composable
@AllowNoModifier
inline fun <reified VM : IViewModel> rememberViewModel(
    type: Any,
    transitionId: String,
    scope: Scope,
): VM = remember {
    val storage: ViewModelStorage = scope.get()
    storage.getOrCreate<VM>(type = type, transactionId = transitionId, provider = scope.getViewModelProvider<VM>())
}
