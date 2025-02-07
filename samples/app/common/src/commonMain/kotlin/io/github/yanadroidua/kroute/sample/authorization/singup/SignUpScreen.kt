package io.github.yanadroidua.kroute.sample.authorization.singup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.yanadroidua.kroute.navigation.router.IDialogRouter
import io.github.yanadroidua.kroute.navigation.ui.DialogNavigationContainer
import io.github.yanadroidua.kroute.sample.Dialog
import io.github.yanadroidua.kroute.sample.FailureDialog
import io.github.yanadroidua.kroute.sample.LoadingDialog
import io.github.yanadroidua.kroute.sample.constants.Spacing
import io.github.yanadroidua.kroute.sample.constants.screenPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    usernameState: State<String>,
    passwordState: State<String>,
    confirmPasswordState: State<String>,
    buttonEnabledState: State<Boolean>,
    dialogRouter: IDialogRouter<Dialog>,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUp: () -> Unit,
    onBack: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val username by usernameState
    val password by passwordState
    val confirmPassword by confirmPasswordState
    val buttonEnabled by buttonEnabledState
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Sign Up") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .screenPadding(),
                verticalArrangement = Arrangement.spacedBy(space = Spacing.One),
            ) {
                Spacer(modifier = Modifier.height(height = Spacing.Three))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text(text = "Username") },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text(text = "Password") },
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = { passwordVisible = !passwordVisible },
                        ) {
                            if (passwordVisible) {
                                Text(text = "Hide")
                            } else {
                                Text(text = "Show")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text(text = "Confirm Password") },
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = { passwordVisible = !passwordVisible },
                        ) {
                            if (passwordVisible) {
                                Text(text = "Hide")
                            } else {
                                Text(text = "Show")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSignUp() }),
                )
                Spacer(modifier = Modifier.height(height = Spacing.One))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSignUp,
                    enabled = buttonEnabled,
                ) {
                    Text(text = "Sign up")
                }
            }

            DialogNavigationContainer(router = dialogRouter) { dialog, _ ->
                when (dialog) {
                    is Dialog.FailureDialog -> FailureDialog(
                        title = dialog.title,
                        message = dialog.message,
                        button = dialog.button,
                        onDismissRequest = onDismissDialog,
                    )

                    Dialog.LoadingDialog -> LoadingDialog()
                }
            }
        }
    }
}
