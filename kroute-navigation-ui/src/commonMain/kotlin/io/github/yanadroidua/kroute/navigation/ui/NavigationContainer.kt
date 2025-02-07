package io.github.yanadroidua.kroute.navigation.ui

import androidx.compose.animation.AnimatedContent
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
import io.github.yanadroidua.kroute.navigation.arguments.ITransitionArguments
import io.github.yanadroidua.kroute.navigation.router.IRouter

@Immutable
private data class TransitionSpec(
    val zIndex: Float,
)

/**
 * A navigation container for the [router][IRouter] that handles the transition and changes the
 * composables based on the transition's route.
 *
 * @param router dialog router
 * @param modifier modifier
 * @param closeKeyboardOnTransition whether keyboard should be closed when performing a transition
 * @param removeFocusOnTransition whether the focus should be removed on transition
 * @param viewFactory composables factory that should provide a UI for each route/destination
 */
@Composable
fun <R : Any> NavigationContainer(
    router: IRouter<R>,
    modifier: Modifier = Modifier,
    closeKeyboardOnTransition: Boolean = true,
    removeFocusOnTransition: Boolean = true,
    viewFactory: @Composable NavigationViewScope<R>.(R, ITransitionArguments) -> Unit,
) {
    val currentTransition by router.currentTransition.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var transitionSpec by remember { mutableStateOf(TransitionSpec(zIndex = 0f)) }

    val isRouterProcessingTransition by remember { derivedStateOf { router.isPerformingOperation } }
    var isHandlingTransition by remember { mutableStateOf(false) }

    LaunchedEffect(router) {
        for (ongoingTransition in router.ongoingOperation) {
            isHandlingTransition = true

            transitionSpec = TransitionSpec(
                zIndex = if (ongoingTransition.isBack) transitionSpec.zIndex - 1f else transitionSpec.zIndex + 1f,
            )

            if (closeKeyboardOnTransition) {
                runCatching { keyboardController?.hide() }
            }
            if (removeFocusOnTransition) {
                runCatching { focusManager.clearFocus(force = true) }
            }

            runCatching { ongoingTransition.start() }
            isHandlingTransition = false
        }
    }

    AnimatedContent(
        modifier = modifier,
        targetState = currentTransition,
        transitionSpec = {
            if (targetState.expedite) {
                ContentTransform(
                    initialContentExit = ExitTransition.None,
                    targetContentEnter = EnterTransition.None,
                    sizeTransform = null,
                )
            } else {
                ContentTransform(
                    initialContentExit = targetState.currentExitAnimation,
                    targetContentEnter = targetState.enterAnimation,
                    sizeTransform = null,
                    targetContentZIndex = transitionSpec.zIndex,
                )
            }
        },
        contentKey = { it.destination to it.id },
    ) { transition ->
        val transitionArguments = remember(transition) {
            TransitionArgumentsManager(
                transition = transition,
                argumentHolder = router,
            )
        }
        val isTransitionRunning by remember { derivedStateOf { this.transition.isRunning } }
        val navigationScope: NavigationViewScope<R> = remember(transition.id) {
            NavigationScopeImpl(transitionId = transition.id, type = transition.destination, animatedVisibilityScope = this)
        }

        CompositionLocalProvider(LocalAnimatedContentScope provides this) {
            Box(
                modifier = Modifier.allowUserInput(
                    allowInput = !isTransitionRunning && !isHandlingTransition && !isRouterProcessingTransition,
                ),
            ) {
                with(navigationScope) {
                    viewFactory(transition.destination, transitionArguments)
                }
            }
        }
    }
}
