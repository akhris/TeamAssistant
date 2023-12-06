package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppTheme

@Composable
fun TextOptionallyEditable(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    onTextEdited: ((String) -> Unit)? = null,
) {

    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onTextEdited?.invoke(it) },
        readOnly = onTextEdited == null,
        decorationBox = { innerTextField ->
            Column {
                innerTextField()
                if (onTextEdited != null) {
                    Box(
                        modifier
                            .height(2.dp)
                            .background(color = Color.Gray)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun previewEditableText() {
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextOptionallyEditable(
                text = "test text"
            )
            TextOptionallyEditable(
                text = "test text",
                onTextEdited = {}
            )
        }
    }
}