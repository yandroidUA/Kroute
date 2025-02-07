package com.yanadroid.kroute.navigation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yanadroid.kroute.navigation.arguments.ITransitionArguments
import com.yanadroid.kroute.navigation.router.DialogState
import com.yanadroid.kroute.navigation.router.IDialogRouter

@Composable
fun <R : Any> DialogNavigationContainer(
    router: IDialogRouter<R>,
    modifier: Modifier = Modifier,
    closeKeyboardOnTransition: Boolean = true,
    removeFocusOnTransition: Boolean = true,
    viewFactory: @Composable AnimatedContentScope.(R, ITransitionArguments) -> Unit,
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
