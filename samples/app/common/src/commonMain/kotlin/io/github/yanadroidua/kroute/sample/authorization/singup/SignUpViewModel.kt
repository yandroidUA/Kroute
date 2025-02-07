package io.github.yanadroidua.kroute.sample.authorization.singup

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import io.github.yanadroidua.kroute.navigation.router.IDialogRouter
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.Dialog
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute
import io.github.yanadroidua.kroute.sample.authorization.signin.SignInArgument
import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    private val authorizationRouter: IScreenRouter<AuthorizationRoute>,
) : CoroutineViewModel() {

    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _confirmPassword = MutableStateFlow("")

    val username: StateFlow<String> = _username.asStateFlow()
    val password: StateFlow<String> = _password.asStateFlow()
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()
    val dialogRouter: IDialogRouter<Dialog> = IDialogRouter.router()

    val isButtonEnabled: Flow<Boolean> = combine(
        _username,
        _password,
        _confirmPassword,
    ) { name, password, confirmPassword ->
        name.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword
    }

    fun onUsernameChange(username: String) {
        _username.update { username }
    }

    fun onPasswordChange(password: String) {
        _password.update { password }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.update { confirmPassword }
    }

    fun back() = launch {
        authorizationRouter.back {
            enterAnimation(EnterTransition.None)
            currentExitAnimation(slideOutHorizontally { it })
        }
    }

    fun signUp() = launch {
        dialogRouter.open(Dialog.LoadingDialog)

        val username = _username.value
        val password = _password.value
        val confirmPassword = _confirmPassword.value

        when {
            username.isBlank() -> {
                dialogRouter.open(
                    Dialog.FailureDialog(
                        title = "No username",
                        message = "Please enter a username in order to sign up in our services.",
                        button = "Ok",
                    ),
                )
                return@launch
            }
            password.isBlank() -> {
                dialogRouter.open(
                    Dialog.FailureDialog(
                        title = "No password",
                        message = "Please enter a password in order to sign up in our services.",
                        button = "Ok",
                    ),
                )
                return@launch
            }
            password != confirmPassword -> {
                dialogRouter.open(
                    Dialog.FailureDialog(
                        title = "Password mismatch",
                        message = "Please enter a correct confirmation password in order to sign up in our services.",
                        button = "Ok",
                    ),
                )
                return@launch
            }
        }

        delay(4_000L)
        toSignIn(username = username, password = password)
    }

    fun onDismissDialog() = launch {
        dialogRouter.close()
    }

    override fun onPause() {
        launch { dialogRouter.close() }
        super.onPause()
    }

    private fun toSignIn(username: String, password: String) = launch {
        if (authorizationRouter.isInStack(route = AuthorizationRoute.SignIn)) {
            // we want to check whether the sign-in is in stack and pop back to it rather than spawn a new instance
            authorizationRouter.popTo(route = AuthorizationRoute.SignIn) {
                enterAnimation(fadeIn())
                currentExitAnimation(slideOutHorizontally { it })
                putArgument(SignInArgument.Password, password)
                putArgument(SignInArgument.Username, username)
            }
            return@launch
        }

        authorizationRouter.goTo(route = AuthorizationRoute.SignIn) {
            enterAnimation(fadeIn())
            currentExitAnimation(slideOutHorizontally { it })
            putArgument(SignInArgument.Password, password)
            putArgument(SignInArgument.Username, username)
        }
    }
}
