package com.yanadroid.kroute.navigation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.yanadroid.kroute.navigation.arguments.ITransitionArguments
import com.yanadroid.kroute.navigation.router.IRouter

@Immutable
private data class TransitionSpec(
    val enterTransition: EnterTransition,
    val exitTransition: ExitTransition,
    val zIndex: Float,
)

@Composable
fun <R : Any> NavigationContainer(
    router: IRouter<R>,
    modifier: Modifier = Modifier,
    closeKeyboardOnTransition: Boolean = true,
    removeFocusOnTransition: Boolean = true,
    viewFactory: @Composable AnimatedContentScope.(R, ITransitionArguments) -> Unit,
) {
    val currentTransition by router.currentTransition.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var transitionSpec by remember {
        mutableStateOf(
            TransitionSpec(
                enterTransition = currentTransition.enterAnimation,
                exitTransition = currentTransition.currentExitAnimation,
                zIndex = 0f,
            ),
        )
    }

    val isRouterProcessingTransition by remember { derivedStateOf { router.isPerformingOperation } }
    var isHandlingTransition by remember { mutableStateOf(false) }

    LaunchedEffect(router) {
        for (ongoingTransition in router.ongoingOperation) {
            isHandlingTransition = true
            val transition = ongoingTransition.transition

            transitionSpec = TransitionSpec(
                enterTransition = transition.enterAnimation,
                exitTransition = transition.currentExitAnimation,
                zIndex = if (ongoingTransition.isBack) transitionSpec.zIndex - 1 else 0f,
            )

            if (closeKeyboardOnTransition) {
                keyboardController?.hide()
            }
            if (removeFocusOnTransition) {
                focusManager.clearFocus(force = true)
            }

            ongoingTransition.start()
            isHandlingTransition = false
        }
    }

    AnimatedContent(
        modifier = modifier,
        targetState = currentTransition,
        transitionSpec = {
            if (currentTransition.expedite) {
                ContentTransform(
                    initialContentExit = ExitTransition.None,
                    targetContentEnter = EnterTransition.None,
                    sizeTransform = null,
                )
            } else {
                ContentTransform(
                    initialContentExit = transitionSpec.exitTransition,
                    targetContentEnter = transitionSpec.enterTransition,
                    sizeTransform = null,
                    targetContentZIndex = transitionSpec.zIndex,
                )
            }
        },
        contentKey = { it.destination },
    ) { transition ->
        val transitionArguments = remember(transition) {
            TransitionArgumentsManager(
                transition = transition,
                argumentHolder = router,
            )
        }
        val destination by remember { derivedStateOf { transition.destination } }
        val isTransitionRunning by remember { derivedStateOf { this.transition.isRunning } }

        CompositionLocalProvider(LocalAnimatedContentScope provides this) {
            Box(
                modifier = Modifier.allowUserInput(
                    allowInput = !isTransitionRunning && !isHandlingTransition && !isRouterProcessingTransition,
                ),
            ) {
                viewFactory(destination, transitionArguments)
            }
        }
    }
}
