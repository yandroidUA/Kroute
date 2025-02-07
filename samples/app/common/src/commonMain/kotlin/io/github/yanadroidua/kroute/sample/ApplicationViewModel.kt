package io.github.yanadroidua.kroute.sample

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel

class ApplicationViewModel(
    val applicationRouter: IScreenRouter<AppRoute>,
) : CoroutineViewModel() {

    fun back() = launch {
        applicationRouter.back {
            enterAnimation(fadeIn())
            currentExitAnimation(fadeOut())
        }
    }
}
