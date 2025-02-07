package io.github.yanadroidua.kroute.sample.main

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel

class MainViewModel(
    private val applicationRouter: IScreenRouter<AppRoute>,
) : CoroutineViewModel() {

    fun toWelcome() = launch {
        applicationRouter.replaceAll(AppRoute.Welcome) {
            enterAnimation(fadeIn())
            currentExitAnimation(fadeOut())
        }
    }
}
