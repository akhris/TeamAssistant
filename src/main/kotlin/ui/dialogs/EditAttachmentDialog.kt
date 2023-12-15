package ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import domain.valueobjects.Attachment
import ui.dialogs.file_picker_dialog.fileChooserDialog
import ui.fields.EditableTextField
import ui.theme.DialogSettings
import utils.getIconPath

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditAttachmentDialog(
    initialAttachment: Attachment? = null,
    onAttachmentPicked: (Attachment) -> Unit,
    onDismiss: () -> Unit,
) {

    var tempAttachment by remember(initialAttachment) { mutableStateOf(initialAttachment ?: Attachment.File()) }

    var field1 by remember(tempAttachment) { mutableStateOf(tempAttachment.name) }
    var field2 by remember(tempAttachment) { mutableStateOf(tempAttachment.description) }
    var field3 by remember(tempAttachment) { mutableStateOf(getField3Text(tempAttachment)) }

    val dialogState = rememberDialogState(
        size = DpSize(
            width = DialogSettings.defaultWideDialogWidth,
            height = DialogSettings.defaultWideDialogHeight
        )
    )

    var isError by remember(field1, field3) { mutableStateOf(field3.isEmpty() or field1.isEmpty()) }

    BaseDialogWindow(
        state = dialogState,
        onCloseRequest = onDismiss,
        title = { Text(text = "добавить вложение") },
        buttons = {
            TextButton(onClick = onDismiss) {
                Text(text = "отмена".uppercase())
            }
            Button(onClick = {
                onAttachmentPicked(
                    tempAttachment
                        .setField1Text(field1)
                        .setField2Text(field2)
                        .setField3Text(field3)
                )
                onDismiss()
            }, content = {
                Text(text = "ок".uppercase())
            }, enabled = !isError)
        },
        content = {

            var showTypeDropDown by remember { mutableStateOf(false) }

            Column {
                ListItem(
                    modifier = Modifier.clickable {
                        showTypeDropDown = !showTypeDropDown
                    },
                    overlineText = { Text("тип вложения") },
                    trailing = {
                        IconButton(onClick = {
                            showTypeDropDown = !showTypeDropDown
                        }) {
                            Icon(
                                modifier = Modifier.rotate(
                                    when (showTypeDropDown) {
                                        true -> 180f
                                        false -> 0f
                                    }
                                ), imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = showTypeDropDown,
                            onDismissRequest = { showTypeDropDown = false }
                        ) {
                            RenderMenuItem(iconRes = "vector/description_black_24dp.svg", text = "файл") {
                                tempAttachment = Attachment.File()
                                showTypeDropDown = false
                            }
                            RenderMenuItem(iconRes = "vector/folder_black_24dp.svg", text = "папка") {
                                tempAttachment = Attachment.Folder()
                                showTypeDropDown = false
                            }
                            RenderMenuItem(iconRes = "vector/email_black_24dp.svg", text = "e-mail") {
                                tempAttachment = Attachment.Email()
                                showTypeDropDown = false
                            }
                            RenderMenuItem(iconRes = "vector/link_black_24dp.svg", text = "ссылка") {
                                tempAttachment = Attachment.InternetLink()
                                showTypeDropDown = false
                            }
                        }
                    }
                ) {
                    Text(
                        when (tempAttachment) {
                            is Attachment.Email -> "e-mail"
                            is Attachment.File -> "файл"
                            is Attachment.Folder -> "папка"
                            is Attachment.InternetLink -> "интернет-ссылка"
                        }
                    )
                }

                //first text field
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = field1,
                    isEditable = true,
                    onValueChange = {
                        field1 = it
                    },
                    label = "имя",
                    isError = field1.isEmpty()
                )
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = field2,
                    isEditable = true,
                    onValueChange = {
                        field2 = it
                    },
                    label = "описание"
                )
                Row {
                    EditableTextField(
                        modifier = Modifier.weight(1f),
                        value = field3,
                        isEditable = true,
                        onValueChange = {
                            field3 = it
                        },
                        label = getField3Label(tempAttachment),
                        isError = field3.isEmpty()
                    )
                    RenderPickerIcon(tempAttachment, onField3Changed = {
                        field3 = it
                    })
                }
            }


        }
    )
}

private fun getField3Text(attachment: Attachment): String {
    return when (attachment) {
        is Attachment.Email -> attachment.email
        is Attachment.File -> attachment.path
        is Attachment.Folder -> attachment.path
        is Attachment.InternetLink -> attachment.link
    }
}

private fun Attachment.setField1Text(name: String): Attachment {
    return when (this) {
        is Attachment.Email -> copy(name = name)
        is Attachment.File -> copy(name = name)
        is Attachment.Folder -> copy(name = name)
        is Attachment.InternetLink -> copy(name = name)
    }
}

private fun Attachment.setField2Text(description: String): Attachment {
    return when (this) {
        is Attachment.Email -> copy(description = description)
        is Attachment.File -> copy(description = description)
        is Attachment.Folder -> copy(description = description)
        is Attachment.InternetLink -> copy(description = description)
    }
}

private fun Attachment.setField3Text(text: String): Attachment {
    return when (this) {
        is Attachment.Email -> copy(email = text)
        is Attachment.File -> copy(path = text)
        is Attachment.Folder -> copy(path = text)
        is Attachment.InternetLink -> copy(link = text)
    }
}

private fun getField3Label(attachment: Attachment): String {
    return when (attachment) {
        is Attachment.Email -> "адрес e-mail"
        is Attachment.File -> "путь к файлу"
        is Attachment.Folder -> "путь к папке"
        is Attachment.InternetLink -> "ссылка"
    }
}


@Composable
private fun RowScope.RenderPickerIcon(attachment: Attachment, onField3Changed: (String) -> Unit) {
    var fileAttachmentPicker by remember { mutableStateOf<Attachment.File?>(null) }
    var folderAttachmentPicker by remember { mutableStateOf<Attachment.Folder?>(null) }

    when (attachment) {
        is Attachment.File -> {
            IconButton(onClick = {
                fileAttachmentPicker = attachment
            }) {
                Icon(painter = painterResource(attachment.getIconPath()), contentDescription = "выбрать файл")
            }
        }

        is Attachment.Folder -> {
            IconButton(onClick = {
                folderAttachmentPicker = attachment
            }) {
                Icon(painter = painterResource(attachment.getIconPath()), contentDescription = "выбрать папку")
            }
        }

        else -> {
            //render nothing
        }
    }

    fileAttachmentPicker?.let { fap ->
        val file = remember {
            fileChooserDialog(
                title = "выберите файл"
            )
        }

        onField3Changed(file)
        fileAttachmentPicker = null
    }

    folderAttachmentPicker?.let { fap ->
        val folder = remember {
            fileChooserDialog(
                title = "выберите папку",
                folderSelection = true
            )
        }

        onField3Changed(folder)
        folderAttachmentPicker = null
    }

}

@Composable
private fun ColumnScope.RenderMenuItem(iconRes: String, text: String, onClick: () -> Unit) {
    DropdownMenuItem(onClick = onClick) {
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp),
            painter = painterResource(iconRes),
            contentDescription = "attachment: $text"
        )
        Text(text = text)
    }
}