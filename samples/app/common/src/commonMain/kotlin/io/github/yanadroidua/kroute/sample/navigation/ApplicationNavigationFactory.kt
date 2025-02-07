package io.github.yanadroidua.kroute.sample.navigation

import io.github.yanadroidua.kroute.lifecycle.koin.DiScopeRouteMapper
import io.github.yanadroidua.kroute.lifecycle.koin.installDiScopeExtension
import io.github.yanadroidua.kroute.lifecycle.viewmodel.ViewModelStorage
import io.github.yanadroidua.kroute.lifecycle.viewmodel.installViewmodelExtension
import io.github.yanadroidua.kroute.navigation.router.IRouteExit
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.AppRoute
import org.koin.core.Koin

fun applicationNavigation(
    initialDestination: AppRoute,
    routerExit: IRouteExit,
    compositeViewModelFactory: CompositeViewModelFactory,
    diScopeMapper: DiScopeRouteMapper<AppRoute>,
    storage: ViewModelStorage,
    koin: Koin,
): IScreenRouter<AppRoute> = IScreenRouter.router(
    routeExit = routerExit,
    initialRoute = initialDestination,
)
    .installViewmodelExtension(
        factory = compositeViewModelFactory,
        storage = storage,
    )
    .installDiScopeExtension(
        diScopeRouteMapper = diScopeMapper,
        koin = koin,
    )
