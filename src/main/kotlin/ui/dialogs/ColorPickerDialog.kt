package ui.dialogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import ui.UiSettings
import ui.fields.CircleIconButton
import ui.theme.DialogSettings

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPickerDialog(
    initialSelection: Color? = null,
    onColorPicked: (Color?) -> Unit,
    onDismiss: () -> Unit,
    circlesSize: Dp = UiSettings.ColorPicker.colorCircleSize,
    colors: List<Color> = listOf(
        Color.DarkGray,
        Color.Green,
        Color.Blue,
        Color.Cyan,
        Color.Gray,
        Color.Magenta,
        Color.Red,
        Color.Yellow
    ),
) {

    val dialogState = rememberDialogState(
        size = DpSize(
            width = DialogSettings.DatePickerSettings.defaultPickerWidth,
            height = DialogSettings.DatePickerSettings.defaultPickerHeight
        )
    )

    var selectedColor by remember(initialSelection) { mutableStateOf(initialSelection) }

    BaseDialogWindow(
        state = dialogState,
        onCloseRequest = onDismiss,
        title = {
            Text("Выберите цвет")
        },
        buttons = {
            Button(onClick = {
                onColorPicked(selectedColor)
                onDismiss()
            }, content = {
                Text(text = "ок".uppercase())
            })
            TextButton(onClick = {
                onDismiss()
            }, content = {
                Text(text = "отмена".uppercase())
            })

        },
        content = {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize().padding(vertical = 8.dp).verticalScroll(rememberScrollState())
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(circlesSize)
                            .clip(CircleShape)
                            .background(color)
                            .border(1.dp, MaterialTheme.colors.onBackground, CircleShape)
                            .clickable { selectedColor = color },
                        contentAlignment = Alignment.Center,
                        content = {
                            if (color == selectedColor) {
                                Image(
                                    Icons.Rounded.Done,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(color, blendMode = BlendMode.ColorDodge),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize(0.75f)
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}