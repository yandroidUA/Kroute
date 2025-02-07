package com.yanadroid.kroute.navigation.ui

import android.app.Activity
import com.yanadroid.kroute.navigation.router.IRouteExit

class AndroidApplicationExit(
    private val activity: Activity,
) : IRouteExit {

    override suspend fun onExit() {
        activity.finish()
    }
}
