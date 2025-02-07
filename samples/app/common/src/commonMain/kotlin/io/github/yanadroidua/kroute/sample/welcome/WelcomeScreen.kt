package io.github.yanadroidua.kroute.sample.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yanadroidua.kroute.sample.constants.screenPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WelcomeScreen(
    toAuthorization: () -> Unit,
    modifier: Modifier = Modifier,
) = Scaffold(
    modifier = modifier,
    topBar = { TopAppBar(title = { Text(text = "Welcome") }) },
) { innerPadding ->
    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .screenPadding(),
    ) {
        Text(text = "This is a Welcome")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = toAuthorization,
        ) {
            Text(text = "Authorization")
        }
    }
}
