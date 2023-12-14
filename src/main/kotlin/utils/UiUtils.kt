package utils

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle

@Composable
fun applyTextStyle(
    textStyle: TextStyle,
    contentAlpha: Float = ContentAlpha.high,
    text: @Composable (() -> Unit)?,
): @Composable (() -> Unit)? {
    if (text == null) return null
    return {
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            ProvideTextStyle(textStyle, text)
        }
    }
}