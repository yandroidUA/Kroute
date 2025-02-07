package com.yanadroid.kroute.navigation.ui

import com.yanadroid.kroute.navigation.router.IRouteExit

class IOSApplicationExit : IRouteExit {
    override suspend fun onExit() = Unit
}
