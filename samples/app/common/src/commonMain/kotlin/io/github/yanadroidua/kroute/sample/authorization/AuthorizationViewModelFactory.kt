package io.github.yanadroidua.kroute.sample.authorization

import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute
import io.github.yanadroidua.kroute.sample.authorization.signin.SignInViewModel
import io.github.yanadroidua.kroute.sample.authorization.singup.SignUpViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModel
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory
import io.github.yanadroidua.kroute.viewmodel.IViewModelProvider

class AuthorizationViewModelFactory(
    private val signInViewModelProvider: IViewModelProvider<SignInViewModel>,
    private val signUpViewModelProvider: IViewModelProvider<SignUpViewModel>,
) : IViewModelFactory<AuthorizationRoute> {
    override fun viewModel(route: AuthorizationRoute): IViewModel = when (route) {
        AuthorizationRoute.SignIn -> signInViewModelProvider.create()
        AuthorizationRoute.SignUp -> signUpViewModelProvider.create()
    }
}
