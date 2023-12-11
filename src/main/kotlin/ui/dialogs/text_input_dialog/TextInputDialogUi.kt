package ui.dialogs.text_input_dialog

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.dialogs.IDialogComponent
import utils.log

@Composable
fun TextInputDialogUi(
    component: IDialogComponent.ITextInputDialogComponent,
    onOkClicked: (edittext: String) -> Unit
) {


    val initialText = remember(component) { component.properties.initialText }
    var editedText by remember { mutableStateOf(initialText) }

    val hint = remember(component) { component.properties.hint }
    val okButtonText = remember(component) { component.properties.OKButtonText }
    val title = remember(component) { component.properties.title }
    AlertDialog(
        modifier = Modifier.onKeyEvent {
            if (it.key == Key.Enter) {
                onOkClicked(editedText)
                component.onDismiss()
                true
            } else false
        },
        onDismissRequest = { component.onDismiss() },
        text = {
            RenderEditText(initialValue = initialText, onValueChanged = {
                log("text edited: $it")
                editedText = it
            }, hint = hint)
        },
        confirmButton = {
            Button(onClick = {
                if (editedText != initialText) {
                    onOkClicked(editedText)
                }
                component.onDismiss()
            }, content = { Text(okButtonText) })
        },
        title = {
            Text(title)
        },
        shape = MaterialTheme.shapes.medium
    )

}

@Composable
private fun RenderEditText(initialValue: String, onValueChanged: (String) -> Unit, hint: String) {
    var editText by remember { mutableStateOf(initialValue) }
    val requester = remember { FocusRequester() }
    TextField(
        modifier = Modifier.focusRequester(requester),
        singleLine = true,
        value = editText,
        onValueChange = { editText = it },
        label = { Text(hint) })

    LaunchedEffect(editText) {
        if (editText == initialValue) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)
        onValueChanged(editText)
    }

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}