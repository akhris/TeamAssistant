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

@Composable
fun TextInputDialogUi(
    component: IDialogComponent.ITextInputDialogComponent,
    onOkClicked: (edittext: String) -> Unit,
) {


    var text by remember(component) { mutableStateOf(component.properties.initialText) }

    val hint = remember(component) { component.properties.hint }
    val okButtonText = remember(component) { component.properties.OKButtonText }
    val title = remember(component) { component.properties.title }

    RenderTextInputDialog(
        title = title,
        hint = hint,
        okButtonText = okButtonText,
        initialValue = text,
        onTextEdited = {
            text = it
            onOkClicked(it)
        },
        onDismiss = { component.onDismiss() }
    )

}

@Composable
fun RenderTextInputDialog(
    title: String = "",
    hint: String = "",
    okButtonText: String = "ок",
    initialValue: String = "",
    onDismiss: () -> Unit,
    onTextEdited: (String) -> Unit,
) {
    var tempText by remember(initialValue) { mutableStateOf(initialValue) }

    AlertDialog(
        modifier = Modifier.onKeyEvent {
            if (it.key == Key.Enter) {
                if (tempText != initialValue) {
                    onTextEdited(tempText)
                }
                onDismiss()
                true
            } else false
        },
        onDismissRequest = { onDismiss() },
        text = {
            RenderEditText(initialValue = tempText, onValueChanged = {
                tempText = it
            }, hint = hint)
        },
        confirmButton = {
            Button(onClick = {
                if (tempText != initialValue) {
                    onTextEdited(tempText)
                }
                onDismiss()
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