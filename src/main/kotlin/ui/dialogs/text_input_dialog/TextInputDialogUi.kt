package ui.dialogs.text_input_dialog

import androidx.compose.material.*
import androidx.compose.runtime.*
import ui.dialogs.IDialogComponent

@Composable
fun TextInputDialogUi(
    component: IDialogComponent.ITextInputDialogComponent,
    onOkClicked: (edittext: String) -> Unit
) {
    var editText by remember { mutableStateOf("") }

    val hint = remember(component) { component.hint }
    val okButtonText = remember(component) { component.OKButtonText }
    val title = remember(component) { component.title }
    AlertDialog(
        onDismissRequest = { component.onDismiss() },
        text = {
            TextField(
                value = editText,
                onValueChange = { editText = it },
                label = { Text(hint) })
        },
        confirmButton = {
            Button(onClick = {
                onOkClicked(editText)
                component.onDismiss()
            }, content = { Text(okButtonText) })
        },
        title = {
            Text(title)
        },
        shape = MaterialTheme.shapes.medium
    )
}