package ui.screens.task_details

import LocalCurrentUser
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.SubTask
import domain.Task
import domain.valueobjects.Attachment
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.dialogs.EditAttachmentDialog
import ui.dialogs.DatePickerDialog
import ui.fields.CircleIconButton
import ui.fields.DateTimeChip
import ui.fields.EditableTextField
import ui.screens.BaseDetailsScreen
import ui.screens.master_detail.IDetailsComponent
import java.time.DayOfWeek

@Composable
fun TaskDetailsUi(component: IDetailsComponent<Task>) {
    val task by remember(component) { component.item }.collectAsState(null)
    val user = LocalCurrentUser.current ?: return
    task?.let {

        //only creator can edit the task
        RenderTaskDetails(
            it,
            isEditable = listOfNotNull(it.creator).contains(user),
            onTaskUpdated = { updatedTask->
                component.updateItem(updatedTask)
            }
        )

    }
}


// https://dribbble.com/shots/5541961-Projecto-Desktop-Task-Management-App
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderTaskDetails(task: Task, isEditable: Boolean, onTaskUpdated: (Task) -> Unit) {
    var tempTask by remember(task) { mutableStateOf(task) }

    var showDatePicker by remember { mutableStateOf(false) }

    BaseDetailsScreen(
        mainTag = tempTask.project?.let { p ->
            {
                Chip(
                    shape = UiSettings.DetailsScreen.tagsShape,
                    onClick = {},
                    content = { Text(p.name) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = p.color?.let { Color(it) }
                            ?: MaterialTheme.colors.onSurface
                    )
                )
            }
        },
        secondaryTag = if (tempTask.targetDate != null || isEditable) {
            {
                if (tempTask.targetDate == null) {
                    TextButton(onClick = { showDatePicker = true }, content = { Text("установить срок выполнения") })
                } else
                    DateTimeChip(
                        dateTime = tempTask.targetDate,
                        label = "срок выполнения",
                        isEditable = isEditable,
                        shape = UiSettings.DetailsScreen.tagsShape,
                        onDateTimeChanged = { tempTask = tempTask.copy(targetDate = it) })
            }
        } else null,
        attachments = {
            RenderAttachments(
                attachments = tempTask.attachments,
                isEditable = isEditable,
                onAttachmentsEdited = {
                    tempTask = tempTask.copy(attachments = it)
                })
        },
        title = {
            EditableTextField(
                modifier = Modifier.fillMaxWidth(),
                value = tempTask.name,
                isEditable = isEditable,
                textStyle = MaterialTheme.typography.h4,
                onValueChange = {
                    tempTask = tempTask.copy(name = it)
                },
                label = if (tempTask.name.isEmpty()) "имя задачи" else ""
            )
        },
        description = if (tempTask.description.isNotEmpty() || isEditable) {
            {
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tempTask.description,
                    onValueChange = {
                        tempTask = tempTask.copy(description = it)
                    },
                    textStyle = MaterialTheme.typography.body1,
                    label = if (tempTask.description.isEmpty()) "описание" else "",
                    isEditable = isEditable
                )
            }
        } else null,
        rightPanel = {
            listOfNotNull(task.creator).plus(task.users).forEach { user ->
                CircleIconButton(
                    iconRes = user.avatar.ifEmpty { "vector/users/person_black_24dp.svg" },
                    isSelected = task.creator?.id == user.id
                )
            }
            CircleIconButton(
                iconRes = "vector/add_circle_black_24dp.svg",
                onClick = {
                    //add user to the task
                }
            )
        }
    )
    /*
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(300.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //main card
            item {
                TitledCard {
                    Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        //name
                        EditableTextField(
                            value = tempTask.name,
                            isEditable = isEditable,
                            textStyle = MaterialTheme.typography.h4,
                            onValueChange = {
                                tempTask = tempTask.copy(name = it)
                            },
                            label = if (tempTask.name.isEmpty()) "имя задачи" else ""
                        )
                        if (tempTask.targetDate != null)
                            DateTimeChip(
                                dateTime = tempTask.targetDate,
                                label = "срок выполнения",
                                isEditable = isEditable,
                                onDateTimeChanged = { tempTask = tempTask.copy(targetDate = it) })

                    }
                }
            }

            //description card:
            if (tempTask.description.isNotEmpty()) {
                item {
                    TitledCard(
                        title = {
                            Text("описание")
                        },
                        content = {
                            EditableTextField(
                                value = tempTask.description,
                                onValueChange = {
                                    tempTask = tempTask.copy(description = it)
                                },
                                textStyle = MaterialTheme.typography.body1,
    //                            label = "описание",
                                isEditable = isEditable
                            )
                        }
                    )
                }
            }

            if (task.users.isNotEmpty())
            //users card:
                item {
                    TitledCard(title = { Text("участники") }) {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            task.users.forEach {
                                Chip(onClick = {}, content = {
                                    Text(it.getInitials())
                                }, leadingIcon = {
                                    Icon(
                                        painterResource("vector/person_remove_black_24dp.svg"),
                                        contentDescription = "remove user from task"
                                    )
                                })
                            }
                            Chip(onClick = {}) {
                                Icon(
                                    painterResource("vector/person_add_black_24dp.svg"),
                                    contentDescription = "add users to task"
                                )
                            }
                        }
                    }
                }

            if (task.attachments.isNotEmpty())
            //attachments card:
                item {
                    TitledCard(title = { Text("вложения") }) {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            task.attachments.forEach {
                                RenderAttachment(it, onClick = {})
                            }
                            Chip(onClick = {}) {
                                Icon(
                                    painterResource("vector/attach_file_black_24dp.svg"),
                                    contentDescription = "добавить вложение к задаче"
                                )
                            }
                        }
                    }
                }


            if (task.subtasks.isNotEmpty())
            //subtasks
                item {
                    TitledCard(title = { Text("подзадачи") }) {
                        Column {
                            task.subtasks.forEach {
                                RenderSubTask(subTask = it, onClick = {})
                            }
                            Chip(onClick = {}) {
                                Icon(
                                    painterResource("vector/add_task_black_24dp.svg"),
                                    contentDescription = "добавить подзадачу"
                                )
                            }
                        }
                    }
                }
            if (isEditable) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        //add date button:
                        if (tempTask.targetDate == null)
                            TextButton(onClick = { showDatePicker = true }, content = { Text("добавить срок выполнения") })
                        //add users button:
                        if (task.users.isEmpty())
                            TextButton(onClick = { }, content = { Text("добавить участников") })
                        //add attachments button:
                        if (task.attachments.isEmpty())
                            TextButton(onClick = { }, content = { Text("добавить вложения") })
                        //add subtask button:
                        if (task.subtasks.isEmpty())
                            TextButton(onClick = { }, content = { Text("добавить подзадачу") })
                    }
                }
            }
        }


     */
    if (showDatePicker) {
        DatePickerDialog(
            initialSelection = tempTask.targetDate?.toLocalDate(),
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { newDate ->
                tempTask = tempTask.copy(targetDate = newDate.atTime(0, 0, 0))
            },
            firstWeekDay = DayOfWeek.MONDAY

        )
    }

    LaunchedEffect(tempTask) {
        if (tempTask == task)
            return@LaunchedEffect

        delay(UiSettings.Debounce.debounceTime)
        onTaskUpdated(tempTask)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RenderAttachments(
    attachments: List<Attachment>,
    isEditable: Boolean,
    onAttachmentsEdited: (List<Attachment>) -> Unit,
) {
    var editAttachmentIndex by remember { mutableStateOf(-1) }
    var addAttachment by remember { mutableStateOf(false) }
    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        attachments.forEachIndexed { index, attachment ->
            RenderAttachment(
                attachment,
                onAttachmentClick = {

                },
                isEditable = isEditable,
                onAttachmentEdit = {
                    editAttachmentIndex = index
                },
                onAttachmentRemove = {
                    onAttachmentsEdited(attachments.toMutableList().apply { removeAt(index) })
                }
            )
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (isEditable)
            TextButton(onClick = { addAttachment = true }, content = { Text("добавить вложение") })
    }

    if (editAttachmentIndex >= 0) {
        EditAttachmentDialog(
            initialAttachment = attachments[editAttachmentIndex],
            onAttachmentPicked = { editedAttachment ->
                onAttachmentsEdited(attachments.toMutableList().apply { this[editAttachmentIndex] = editedAttachment })
            },
            onDismiss = { editAttachmentIndex = -1 })
    }

    if (addAttachment) {
        EditAttachmentDialog(
            initialAttachment = Attachment.File(),
            onAttachmentPicked = { addedAttachment ->
                onAttachmentsEdited(attachments.plus(addedAttachment))
            },
            onDismiss = { addAttachment = false })
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun RenderAttachment(
    attachment: Attachment,
    isEditable: Boolean,
    onAttachmentClick: () -> Unit,
    onAttachmentEdit: () -> Unit,
    onAttachmentRemove: () -> Unit,

    ) {

    var isHovered by remember { mutableStateOf(false) }

    Chip(
        modifier = Modifier
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false },
        onClick = onAttachmentClick,
        content = {
            Text(attachment.name)
            if (isHovered && isEditable) {
                IconButton(onClick = onAttachmentEdit,
                    content = { Icon(imageVector = Icons.Rounded.Edit, contentDescription = "edit attachment") })
                IconButton(onClick = onAttachmentRemove,
                    content = { Icon(imageVector = Icons.Rounded.Delete, contentDescription = "delete attachment") })
            }
        },
        leadingIcon = {
            val iconPath = when (attachment) {
                is Attachment.Email -> "vector/email_black_24dp.svg"
                is Attachment.File -> "vector/description_black_24dp.svg"
                is Attachment.Folder -> "vector/folder_black_24dp.svg"
                is Attachment.InternetLink -> "vector/link_black_24dp.svg"
            }
            Icon(painterResource(iconPath), contentDescription = "task attachment")
        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderSubTask(subTask: SubTask, onClick: () -> Unit) {
    ListItem(icon = {
        Icon(imageVector = Icons.Rounded.Check, contentDescription = "отметка выполнения подзадачи")
    }, text = {
        Text(subTask.name)
    }, secondaryText = {
        Text(subTask.description)
    })
}


