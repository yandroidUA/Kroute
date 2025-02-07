package io.github.yanadroidua.kroute.sample.authorization

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.github.yanadroidua.kroute.lint.utils.AllowNoModifier
import io.github.yanadroidua.kroute.navigation.ui.NavigationContainer
import io.github.yanadroidua.kroute.navigation.ui.NavigationViewScope
import io.github.yanadroidua.kroute.navigation.ui.RegisterBackButtonHandler
import io.github.yanadroidua.kroute.navigation.ui.rememberArgumentOrNull
import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute.SignIn
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute.SignUp
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationScope
import io.github.yanadroidua.kroute.sample.authorization.signin.SignInArgument
import io.github.yanadroidua.kroute.sample.authorization.signin.SignInScreen
import io.github.yanadroidua.kroute.sample.authorization.signin.SignInViewModel
import io.github.yanadroidua.kroute.sample.authorization.singup.SignUpScreen
import io.github.yanadroidua.kroute.sample.authorization.singup.SignUpViewModel
import io.github.yanadroidua.kroute.ui.koin.viewmodel.ScopedViewModelComposable
import io.github.yanadroidua.kroute.ui.navigation.koin.viewmodel.rememberViewModel
import io.github.yanadroidua.kroute.ui.viewmodel.ViewModelComposable
import org.koin.core.Koin

/**
 * Authorization navigation router is shared across SignInViewModel and SignUpViewModel, same instance is provided
 * by reusing single scope to instantiate these view models.
 */
@Composable
@AllowNoModifier
fun NavigationViewScope<AppRoute>.AuthorizationFlow(koin: Koin) {
    ScopedViewModelComposable<AuthorizationScope, AuthorizationViewModel>(
        transitionId = transitionId,
        type = type,
        koin = koin,
    ) { scope, viewModel ->

        NavigationContainer(
            modifier = Modifier.fillMaxSize(),
            router = viewModel.authorizationRouter,
        ) { route, args ->
            when (route) {
                SignIn -> ViewModelComposable(
                    viewModel = rememberViewModel<AuthorizationRoute, SignInViewModel>(
                        scope = scope,
                    ),
                ) { vm ->
                    val usernameState = vm.usernameFlow.collectAsState()
                    val passwordState = vm.passwordFlow.collectAsState()
                    val buttonEnabledState = vm.buttonEnabledFlow.collectAsState(initial = false)
                    val mockErrorState = vm.isError.collectAsState()

                    val initialPassword = args.rememberArgumentOrNull(SignInArgument.Password)
                    val initialUsername = args.rememberArgumentOrNull(SignInArgument.Username)

                    LaunchedEffect(initialUsername) {
                        if (initialUsername != null) {
                            vm.onUsernameChange(username = initialUsername)
                        }
                    }

                    LaunchedEffect(initialPassword) {
                        if (initialPassword != null) {
                            vm.onPasswordChange(password = initialPassword)
                        }
                    }

                    SignInScreen(
                        modifier = Modifier.fillMaxSize(),
                        toSignUp = vm::toSignUp,
                        back = vm::back,
                        onPasswordChange = vm::onPasswordChange,
                        onUsernameChange = vm::onUsernameChange,
                        onDismissDialog = vm::dismissDialog,
                        onMockErrorChange = vm::onErrorChange,
                        onSignIn = vm::signIn,
                        usernameState = usernameState,
                        passwordState = passwordState,
                        buttonEnabledState = buttonEnabledState,
                        mockErrorState = mockErrorState,
                        dialogRouter = vm.loadingDialogRouter,
                    )
                }
                SignUp -> ViewModelComposable(
                    viewModel = rememberViewModel<AuthorizationRoute, SignUpViewModel>(
                        scope = scope,
                    ),
                ) { vm ->
                    val usernameState = vm.username.collectAsState()
                    val passwordState = vm.password.collectAsState()
                    val confirmPasswordState = vm.confirmPassword.collectAsState()
                    val signUpButtonEnabled = vm.isButtonEnabled.collectAsState(initial = false)

                    SignUpScreen(
                        modifier = Modifier.fillMaxSize(),
                        usernameState = usernameState,
                        passwordState = passwordState,
                        confirmPasswordState = confirmPasswordState,
                        buttonEnabledState = signUpButtonEnabled,
                        onDismissDialog = vm::onDismissDialog,
                        onUsernameChange = vm::onUsernameChange,
                        onPasswordChange = vm::onPasswordChange,
                        onConfirmPasswordChange = vm::onConfirmPasswordChange,
                        onBack = vm::back,
                        onSignUp = vm::signUp,
                        dialogRouter = vm.dialogRouter,
                    )
                }
            }
        }

        RegisterBackButtonHandler(
            enabled = true,
            onBack = { viewModel.back() },
        )
    }
}
