package io.github.yanadroidua.kroute.navigation.ui

import android.app.Activity
import io.github.yanadroidua.kroute.navigation.router.IRouteExit
import io.github.yanadroidua.kroute.navigation.transition.scope.TransitionScope

class AndroidApplicationExit(
    private val activity: Activity,
) : IRouteExit {

    override suspend fun onExit(configuration: TransitionScope.() -> Unit) {
        activity.finish()
    }
}
