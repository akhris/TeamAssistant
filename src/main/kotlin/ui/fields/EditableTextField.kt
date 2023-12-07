package ui.fields

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditableTextField(
    value: String,
    isEditable: Boolean,
    textStyle: TextStyle,
    label: String = "",
    onValueChange: (String) -> Unit,
) {
    var isHovered by remember { mutableStateOf(false) }

    val textFieldColors =
        if (isHovered && isEditable) {
            TextFieldDefaults.textFieldColors(
//                backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
                backgroundColor = Color.Unspecified,
                focusedIndicatorColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity)
            )
        } else {
            TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Unspecified,
                focusedIndicatorColor = Color.Unspecified,
                unfocusedIndicatorColor = Color.Unspecified
            )
        }

    TextField(
        modifier = Modifier
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
//            .border(
//                width = 2.dp,
//                color = if (isHovered) MaterialTheme.colors.primary else Color.Unspecified,
//                shape = MaterialTheme.shapes.small
//            )
        ,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle,
        readOnly = !isEditable,
        colors = textFieldColors,
        label = if (label.isNotEmpty()) {
            {
                Text(text = label)
            }
        } else null
    )
}