package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yanadroidua.kroute.navigation.arguments.ITransitionArguments
import io.github.yanadroidua.kroute.navigation.router.DialogState
import io.github.yanadroidua.kroute.navigation.router.IDialogRouter

/**
 * A navigation container for the [dialog router][IDialogRouter] that handles the transition and changes the
 * composables based on the transition's route. It automatically handles the [DialogState.Empty] state.
 *
 * @param router dialog router
 * @param modifier modifier
 * @param closeKeyboardOnTransition whether keyboard should be closed when performing a transition
 * @param removeFocusOnTransition whether the focus should be removed on transition
 * @param viewFactory composables factory that should provide a UI for each route/destination
 * @see NavigationContainer
 */
@Composable
fun <R : Any> DialogNavigationContainer(
    router: IDialogRouter<R>,
    modifier: Modifier = Modifier,
    closeKeyboardOnTransition: Boolean = true,
    removeFocusOnTransition: Boolean = true,
    viewFactory: @Composable NavigationViewScope<DialogState<R>>.(R, ITransitionArguments) -> Unit,
) = NavigationContainer(
    modifier = modifier,
    router = router,
    closeKeyboardOnTransition = closeKeyboardOnTransition,
    removeFocusOnTransition = removeFocusOnTransition,
    viewFactory = { destination, arguments ->
        when (destination) {
            DialogState.Empty -> {}
            is DialogState.Present -> viewFactory(destination.route, arguments)
        }
    },
)
