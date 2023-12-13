package ui.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import ui.theme.DialogSettings
import utils.ResourcesUtils
import java.time.LocalDateTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconsPickerDialog(
    iconsFolder: ResourcesUtils.ResourcesFolder,
    initialSelection: String = "",
    onIconPicked: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val icons = remember(iconsFolder) { ResourcesUtils.getResources(iconsFolder) }

    var selection by remember(initialSelection) { mutableStateOf(initialSelection) }

    val dialogState = rememberDialogState(
        size = DpSize(
            width = DialogSettings.TimePickerSettings.defaultPickerWidth,
            height = DialogSettings.TimePickerSettings.defaultPickerInputModeHeight
        )
    )

    DialogWindow(
        state = dialogState,
        onCloseRequest = onDismiss,
        undecorated = true,
        resizable = false,
        transparent = true,
        content = {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        icons.forEach { icon ->
                            OutlinedButton(
                                modifier = Modifier
                                    .border(width = 3.dp,
                                        shape = CircleShape,
                                        color = if (selection == icon) MaterialTheme.colors.primary else Color.Unspecified),
                                shape = CircleShape,
                                onClick = {
                                    selection = icon
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(36.dp),
                                        painter = painterResource(icon),
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                    //buttons
                    Row(modifier = Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = {
                            onIconPicked(
                                selection
                            )
                            onDismiss()
                        }, content = {
                            Text(text = "ок".uppercase())
                        })
                    }
                }
            }
        }
    )


}