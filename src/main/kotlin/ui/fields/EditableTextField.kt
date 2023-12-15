package ui.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Text Field with removed inner paddings and looking like simple Text while not in edit mode.
 * When is hovered by mouse icon - shows indicator line at the bottom and clear text icon (if text is not empty)
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun EditableTextField(
    modifier: Modifier = Modifier,
    value: String,
    isEditable: Boolean,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String = "",
    onValueChange: (String) -> Unit,
    withClearIcon: Boolean = true,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
) {
    var isHovered by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }


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

    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        textFieldColors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
            .background(textFieldColors.backgroundColor(enabled).value, shape)
            .indicatorLine(enabled, isError, interactionSource, textFieldColors)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = mergedTextStyle,
        readOnly = !isEditable,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                label = if (label.isNotEmpty()) {
                    {
                        Text(text = label)
                    }
                } else null,
                trailingIcon = if (withClearIcon && value.isNotEmpty() && isEditable) {
                    {
                        Icon(
                            modifier = Modifier.clickable {
                                onValueChange("")
                            },
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "очистить текст",
                            tint = LocalContentColor.current.copy(alpha = if (isHovered) ContentAlpha.medium else ContentAlpha.disabled)
                        )
                    }
                } else null,
                singleLine = singleLine,
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = textFieldColors,
                visualTransformation = VisualTransformation.None,
                contentPadding = PaddingValues(0.dp)
            )
        }
    )
}
