package io.github.yanadroidua.kroute.sample.constants

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Spacing {
    @Stable
    val Zero = 0.dp

    @Stable
    val Half = 4.dp

    @Stable
    val One = 8.dp

    @Stable
    val Two = 16.dp

    @Stable
    val Three = 24.dp

    @Stable
    val Four = 32.dp

    @Stable
    val HorizontalPadding = Two

    @Stable
    val VerticalPadding = One
}

fun Modifier.screenPadding(): Modifier = padding(
    horizontal = Spacing.HorizontalPadding,
    vertical = Spacing.VerticalPadding,
)
