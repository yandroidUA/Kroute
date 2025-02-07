package io.github.yanadroidua.kroute.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.yanadroidua.kroute.sample.constants.Spacing
import io.github.yanadroidua.kroute.sample.constants.screenPadding

@Immutable
sealed interface Dialog {
    @Immutable
    data object LoadingDialog : Dialog

    @Immutable
    data class FailureDialog(
        val title: String,
        val message: String,
        val button: String,
    ) : Dialog
}

@Composable
internal fun LoadingDialog() {
    Dialog(
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        onDismissRequest = {},
    ) {
        Card(shape = RoundedCornerShape(size = 25.dp)) {
            Box(modifier = Modifier.padding(all = Spacing.Four)) {
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FailureDialog(
    title: String,
    message: String,
    button: String,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit = onDismissRequest,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Card(shape = RoundedCornerShape(size = 25.dp)) {
            Column(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
                Column(
                    modifier = Modifier.screenPadding(),
                    verticalArrangement = Arrangement.spacedBy(space = Spacing.One),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                    Text(text = message)
                }
                HorizontalDivider()
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onButtonClick,
                ) {
                    Text(text = button)
                }
            }
        }
    }
}
