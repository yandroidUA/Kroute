package io.github.yanadroidua.kroute.sample.authorization

import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute
import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel

/**
 * This is shared ViewModel for the whole authorization flow.
 */
class AuthorizationViewModel(
    val authorizationRouter: IScreenRouter<AuthorizationRoute>,
) : CoroutineViewModel() {

    fun back() = launch {
        authorizationRouter.back {
            enterAnimation(fadeIn())
            currentExitAnimation(slideOutHorizontally { it })
        }
    }
}
