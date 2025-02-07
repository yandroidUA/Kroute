package io.github.yanadroidua.kroute.sample.authorization.signin

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import io.github.yanadroidua.kroute.navigation.router.DialogState
import io.github.yanadroidua.kroute.navigation.router.IDialogRouter
import io.github.yanadroidua.kroute.navigation.router.IScreenRouter
import io.github.yanadroidua.kroute.sample.AppRoute
import io.github.yanadroidua.kroute.sample.Dialog
import io.github.yanadroidua.kroute.sample.authorization.di.AuthorizationRoute
import io.github.yanadroidua.kroute.viewmodel.CoroutineViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class SignInViewModel(
    private val applicationRouter: IScreenRouter<AppRoute>,
    private val authorizationRouter: IScreenRouter<AuthorizationRoute>,
) : CoroutineViewModel() {

    private val _passwordFlow = MutableStateFlow("")
    private val _usernameFlow = MutableStateFlow("")
    private val _isError = MutableStateFlow(false)

    private var signInJob: Job? = null

    val loadingDialogRouter: IDialogRouter<Dialog> = IDialogRouter.router()

    val passwordFlow: StateFlow<String> = _passwordFlow.asStateFlow()
    val usernameFlow: StateFlow<String> = _usernameFlow.asStateFlow()
    val isError: StateFlow<Boolean> = _isError.asStateFlow()
    val buttonEnabledFlow: Flow<Boolean> = combine(
        _usernameFlow,
        _passwordFlow,
        loadingDialogRouter.currentTransition,
    ) { username, password, loadingTransition ->
        username.isNotBlank() && password.isNotBlank() && loadingTransition.destination is DialogState.Empty
    }

    fun onPasswordChange(password: String) {
        _passwordFlow.update { password }
    }

    fun onUsernameChange(username: String) {
        _usernameFlow.update { username }
    }

    fun onErrorChange(error: Boolean) {
        _isError.update { error }
    }

    fun signIn() {
        signInJob?.cancel()

        signInJob = launchJob {
            loadingDialogRouter.open(route = Dialog.LoadingDialog)
            delay(5_000L)
            if (_isError.value) {
                loadingDialogRouter.open(
                    route = Dialog.FailureDialog(
                        title = "Failure",
                        message = "This failure is simulated by using a switcher. If you would like this operation to succeed please turn off the error switcher.",
                        button = "Close",
                    ),
                )
            } else {
                loadingDialogRouter.close()
                applicationRouter.replaceAll(route = AppRoute.Main) {
                    enterAnimation(fadeIn())
                    currentExitAnimation(fadeOut())
                }
            }
        }
    }

    fun back() = launch {
        authorizationRouter.back {
            enterAnimation(EnterTransition.None)
            currentExitAnimation(slideOutHorizontally { it })
        }
    }

    fun toSignUp() = launch {
        authorizationRouter.goTo(route = AuthorizationRoute.SignUp) {
            enterAnimation(slideInHorizontally { it })
            currentExitAnimation(fadeOut())
        }
    }

    fun dismissDialog() = launch {
        loadingDialogRouter.close {
            currentExitAnimation(fadeOut())
        }
    }

    override fun onPause() {
        signInJob?.cancel()
        launch { loadingDialogRouter.close() }
        super.onPause()
    }
}
