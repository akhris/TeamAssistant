package ui.dialogs.text_input_dialog

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import ui.dialogs.IDialogComponent

@Composable
fun TextInputDialogUi(component: IDialogComponent.ITextInputDialogComponent) {
    AlertDialog(onDismissRequest = { component.onDismiss() }, text = {
        TextField(
            value = "team 2",
            onValueChange = { },
            label = { Text("имя команды") })
    }, confirmButton = {
        Button(onClick = {
//            if (newSampleTypeName.isNotEmpty()) {
//                val newSampleType = SampleType(name = newSampleTypeName)
//                component.addSampleType(
//                    newSampleType
//                )
            component.onDismiss()
//            }
        }, content = { Text("добавить") })
    }, title = {
        Text("создать команду")
    }, shape = MaterialTheme.shapes.medium)
}