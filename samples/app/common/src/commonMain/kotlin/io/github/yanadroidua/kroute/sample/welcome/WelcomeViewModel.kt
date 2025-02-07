package io.github.yanadroidua.kroute.sample.welcome

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel

class WelcomeViewModel(
    private val router: IScreenRouter<AppRoute>,
) : CoroutineViewModel() {

    fun toAuthentication() = launch {
        router.goTo(route = AppRoute.Authorization) {
            enterAnimation(slideInHorizontally { it })
            currentExitAnimation(ExitTransition.None)
        }
    }
}
