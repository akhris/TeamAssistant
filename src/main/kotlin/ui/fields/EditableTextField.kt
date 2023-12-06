package ui.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditableTextField(text: String, isEditable: Boolean, textStyle: TextStyle, onTextEdited: (String) -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    var editable by remember { mutableStateOf(false) }


    BasicTextField(
        modifier = Modifier
//            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
//            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
            .clickable {
                if (isEditable) {
                    editable = !editable
                }
            },
        value = text,
        onValueChange = {
            onTextEdited(it)
        },
        textStyle = textStyle,
        readOnly = !editable
    )
}