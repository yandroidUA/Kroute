package io.github.yanadroidua.kroute.sample.di

import io.github.yanadroidua.kroute.koin.viewmodel.getViewModelProvider
import io.github.yanadroidua.kroute.koin.viewmodel.viewModelProvider
import io.github.yanadroidua.kroute.lifecycle.koin.DiScopeRouteMapper
import io.github.yanadroidua.kroute.lifecycle.koin.scopeId
import io.github.yanadroidua.kroute.lifecycle.viewmodel.ViewModelStorage
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.sample.ApplicationViewModel
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationScope
import io.github.yanadroidua.kroute.sample.authorization.di.authorizationModule
import io.github.yanadroidua.kroute.sample.main.MainViewModel
import io.github.yanadroidua.kroute.sample.navigation.CompositeViewModelFactory
import io.github.yanadroidua.kroute.sample.navigation.applicationNavigation
import io.github.yanadroidua.kroute.sample.welcome.WelcomeViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory
import io.github.yanadroidua.kroute.viewmodel.IViewModelProvider
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val ApplicationRouterQualifier = named<AppRoute>()
val ApplicationRouteExit = named("ApplicationRouteExit")
val ApplicationDiScope = named("ApplicationDiScopeMapper")

internal fun applicationModule() = module {
    includes(authorizationModule())

    single { ViewModelStorage() }

    single(ApplicationRouterQualifier) {
        applicationNavigation(
            initialDestination = AppRoute.Welcome,
            routerExit = get(ApplicationRouteExit),
            compositeViewModelFactory = get(),
            diScopeMapper = get(ApplicationDiScope),
            storage = get(),
            koin = getKoin(),
        )
    } bind IScreenRouter::class

    single {
        CompositeViewModelFactory(
            welcomeViewModel = getViewModelProvider(),
            authorizationViewModel = getViewModelProvider(),
            mainViewModel = getViewModelProvider(),
        )
    } bind IViewModelFactory::class

    single(ApplicationDiScope) {
        DiScopeRouteMapper<AppRoute> { route ->
            when (route) {
                AppRoute.Welcome -> null
                AppRoute.Main -> null
                AppRoute.Authorization -> scopeId<AuthorizationScope>()
            }
        }
    }
    single {
        ApplicationViewModel(applicationRouter = get(ApplicationRouterQualifier))
    }
    viewModelProvider {
        IViewModelProvider {
            WelcomeViewModel(router = get(ApplicationRouterQualifier))
        }
    }
    viewModelProvider {
        IViewModelProvider {
            MainViewModel(applicationRouter = get(ApplicationRouterQualifier))
        }
    }
}
