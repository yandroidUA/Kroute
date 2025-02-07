package io.github.yanadroidua.kroute.sample.authorization.di

import io.github.yanadroidua.kroute.koin.viewmodel.getViewModelProvider
import io.github.yanadroidua.kroute.koin.viewmodel.viewModelProvider
import io.github.yanadroidua.kroute.lifecycle.koin.scopeId
import io.github.yanadroidua.kroute.lifecycle.viewmodel.installViewmodelExtension
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.sample.authorization.AuthorizationViewModel
import io.github.yanadroidua.kroute.sample.authorization.AuthorizationViewModelFactory
import io.github.yanadroidua.kroute.sample.authorization.signin.SignInViewModel
import io.github.yanadroidua.kroute.sample.authorization.singup.SignUpViewModel
import io.github.yanadroidua.kroute.sample.di.ApplicationRouterQualifier
import io.github.yanadroidua.kroute.viewmodel.IViewModelFactory
import io.github.yanadroidua.kroute.viewmodel.IViewModelProvider
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private val AuthorizationRouterQualifier = named<AuthorizationRoute>()
private val AuthorizationCompositeViewModelFactory = named("AuthorizationCompositeViewModelFactory")

fun authorizationModule() = module {
    viewModelProvider {
        IViewModelProvider {
            AuthorizationViewModel(
                authorizationRouter = getKoin()
                    .getOrCreateScope<AuthorizationScope>(scopeId<AuthorizationScope>())
                    .get(AuthorizationRouterQualifier),
            )
        }
    }

    scope<AuthorizationScope> {
        viewModelProvider {
            IViewModelProvider {
                SignInViewModel(
                    applicationRouter = get(ApplicationRouterQualifier),
                    authorizationRouter = get(AuthorizationRouterQualifier),
                )
            }
        }

        viewModelProvider {
            IViewModelProvider {
                SignUpViewModel(
                    authorizationRouter = get(AuthorizationRouterQualifier),
                )
            }
        }

        factory(AuthorizationCompositeViewModelFactory) {
            AuthorizationViewModelFactory(
                signInViewModelProvider = getViewModelProvider(),
                signUpViewModelProvider = getViewModelProvider(),
            )
        } bind IViewModelFactory::class

        scoped(AuthorizationRouterQualifier) {
            val applicationRouter: IScreenRouter<AppRoute> = get(ApplicationRouterQualifier)

            IScreenRouter.router(
                initialRoute = AuthorizationRoute.SignIn,
                routeExit = { applicationRouter.back(it) },
            ).installViewmodelExtension(
                factory = get(AuthorizationCompositeViewModelFactory),
                // reusing same global VM storage
                storage = get(),
            )
        } bind IScreenRouter::class
    }
}
