package io.github.yanadroidua.kroute.sample.navigation

import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.sample.authorization.AuthorizationViewModel
import io.github.yanadroidua.kroute.sample.main.MainViewModel
import io.github.yanadroidua.kroute.sample.welcome.WelcomeViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory
import io.github.yanadroidua.kroute.viewmodel.IViewModelProvider

class CompositeViewModelFactory(
    private val welcomeViewModel: IViewModelProvider<WelcomeViewModel>,
    private val authorizationViewModel: IViewModelProvider<AuthorizationViewModel>,
    private val mainViewModel: IViewModelProvider<MainViewModel>,
) : IViewModelFactory<AppRoute> {

    override fun viewModel(route: AppRoute): IViewModel? = when (route) {
        AppRoute.Welcome -> welcomeViewModel.create()
        AppRoute.Authorization -> authorizationViewModel.create()
        AppRoute.Main -> mainViewModel.create()
    }
}
