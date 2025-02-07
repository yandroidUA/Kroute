package io.github.yanadroidua.kroute.sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.yanadroidua.kroute.lint.utils.AllowNoModifier
import io.github.yanadroidua.kroute.navigation.ui.NavigationContainer
import io.github.yanadroidua.kroute.navigation.ui.RegisterBackButtonHandler
import io.github.yanadroidua.kroute.sample.authorization.AuthorizationFlow
import io.github.yanadroidua.kroute.sample.main.MainScreen
import io.github.yanadroidua.kroute.sample.main.MainViewModel
import io.github.yanadroidua.kroute.sample.welcome.WelcomeScreen
import io.github.yanadroidua.kroute.sample.welcome.WelcomeViewModel
import io.github.yanadroidua.kroute.ui.navigation.koin.viewmodel.rememberViewModel
import io.github.yanadroidua.kroute.ui.viewmodel.ViewModelComposable

@Composable
@AllowNoModifier
fun Application() {
    val applicationComponent = remember { ApplicationComponent() }

    ViewModelComposable(viewModel = applicationComponent.applicationViewModel) { appViewModel ->
        NavigationContainer(
            modifier = Modifier.fillMaxSize(),
            router = appViewModel.applicationRouter,
            viewFactory = { route, _ ->
                when (route) {
                    AppRoute.Welcome -> {
                        RegisterBackButtonHandler(
                            enabled = true,
                            onBack = { appViewModel.back() },
                        )

                        ViewModelComposable(
                            viewModel = rememberViewModel<AppRoute, WelcomeViewModel>(
                                koin = applicationComponent.getKoin(),
                            ),
                        ) { vm ->
                            WelcomeScreen(
                                toAuthorization = vm::toAuthentication,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }

                    AppRoute.Main -> {
                        RegisterBackButtonHandler(
                            enabled = true,
                            onBack = { appViewModel.back() },
                        )

                        ViewModelComposable(
                            viewModel = rememberViewModel<AppRoute, MainViewModel>(
                                koin = applicationComponent.getKoin(),
                            ),
                        ) { vm ->
                            MainScreen(
                                modifier = Modifier.fillMaxSize(),
                                toWelcome = vm::toWelcome,
                            )
                        }
                    }

                    AppRoute.Authorization -> AuthorizationFlow(koin = applicationComponent.getKoin())
                }
            },
        )
    }
}
