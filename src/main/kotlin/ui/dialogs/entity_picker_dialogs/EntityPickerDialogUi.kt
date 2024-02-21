package ui.dialogs.entity_picker_dialogs

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.rememberDialogState
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.EntitiesListUi
import ui.dialogs.BaseDialogWindow
import ui.theme.DialogSettings

@Composable
fun <T> EntityPickerDialogUi(
    component: IBaseEntityPickerDialogComponent<T>,
    onDismiss: () -> Unit,
) {
    val items by remember(component) { component.items }.subscribeAsState()
    var selection by remember(component) { mutableStateOf(component.initialSelection) }

    val selectionMode = remember(component) { component.selectMode }

    val dialogState = rememberDialogState(
        size = DpSize(
            width = DialogSettings.defaultWideDialogWidth,
            height = DialogSettings.defaultWideDialogHeight
        )
    )

    BaseDialogWindow(
        state = dialogState,
        onCloseRequest = onDismiss,
        title = { Text(text = component.title) },
        buttons = {
            TextButton(onClick = {
                onDismiss()
            }, content = {
                Text(text = "отмена")
            })
            Button(
                onClick = {
                    if (selection != component.initialSelection)
                        component.onItemsSelected(selection)
                    onDismiss()
                },
                content = { Text(text = "выбрать") }
            )
        },
        content = {
            EntitiesListUi(
                list = items,
                selectMode = selectionMode,
                initialSelection = selection,
                itemRenderer = remember(component) { component.itemRenderer }
            ) {
                selection = it
            }
        }
    )
}