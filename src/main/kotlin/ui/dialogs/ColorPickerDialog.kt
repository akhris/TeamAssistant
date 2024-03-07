package ui.dialogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import ui.UiSettings
import ui.theme.DialogSettings
import utils.oppositeColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPickerDialog(
    initialSelection: Color? = null,
    onColorPicked: (Color?) -> Unit,
    onDismiss: () -> Unit,
    circlesSize: Dp = UiSettings.ColorPicker.colorCircleSize,
    colors: List<Color> = listOf(
        Color(0xFF7FFFD4),
        Color(0xFF00BFFF),
        Color(0xFF8A2BE2),
        Color(0xFFDE3163),
        Color(0xFFFDEE00),
        Color(0xFFEF9B0F),
        Color(0xFFAB274F),
        Color(0xFFFF00FF),
        Color(0xFF91A3B0),
        Color(0xFFACE1AF),
        Color(0xFF80FF00),
        Color(0xFFFF4F00),
        Color(0xFFE09540),
        Color(0xFFFCF75E),
        Color(0xFF007FFF),
        Color(0xFF4F7942),
        Color(0xFFFFFF00),
        Color(0xFF808000),
        Color(0xFFFFDEAD),
        Color(0xFF1E2952),


        ).sortedBy { it.toArgb() },
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
                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(circlesSize)
                            .clip(CircleShape)
                            .background(color)
                            .border(1.dp, Color.Unspecified, CircleShape)
                            .clickable { selectedColor = color },
                        contentAlignment = Alignment.Center,
                        content = {
                            if (color == selectedColor) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    tint = color.oppositeColor(),
                                    contentDescription = "selected color",
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