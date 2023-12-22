package ui.dialogs.base_entity_picker_dialog

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.rememberDialogState
import domain.EntitiesList
import ui.EntitiesListUi
import ui.SelectionMode
import ui.dialogs.BaseDialogWindow
import ui.theme.DialogSettings

@Composable
fun <T> EntityPickerDialogUi(
    component: IBaseEntityPickerDialogComponent<T>,
    onDismiss: () -> Unit,
    initialSelection: List<T> = listOf(),
) {
    val items by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    val selectionMode = remember(component.selectMode) {
        when (component.selectMode) {
            SelectMode.MULTIPLE -> SelectionMode.MultiSelection<T>(
                initialSelection = initialSelection,
                onItemsSelected = {

                }
            )

            SelectMode.SINGLE -> SelectionMode.SingleSelection<T>(
                initialSelection = initialSelection.firstOrNull(),
                onItemSelected = {

                }
            )
        }
    }

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
            Button(
                onClick = {

                    onDismiss()
                },
                content = { Text(text = "выбрать") }
            )
        },
        content = {
            EntitiesListUi(
                list = items,
                selectionMode = selectionMode,
                itemRenderer = remember(component) { component.itemRenderer }
            )
        }
    )
}