package io.github.yanadroidua.kroute.sample.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yanadroidua.kroute.sample.constants.Spacing
import io.github.yanadroidua.kroute.sample.constants.screenPadding

@Composable
fun MainScreen(
    toWelcome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).screenPadding(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Three),
        ) {
            Text(text = "Main page")
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = toWelcome,
            ) {
                Text(text = "To Welcome")
            }
        }
    }
}
