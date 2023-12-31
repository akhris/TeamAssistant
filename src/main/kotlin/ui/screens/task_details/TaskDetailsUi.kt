package ui.screens.task_details

import LocalCurrentUser
import LocalNavController
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import domain.SubTask
import domain.Task
import domain.User
import domain.valueobjects.Attachment
import domain.valueobjects.TaskMessage
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.dialogs.EditAttachmentDialog
import ui.dialogs.DatePickerDialog
import ui.dialogs.text_input_dialog.RenderTextInputDialog
import ui.fields.CircleIconButton
import ui.fields.DateTimeChip
import ui.fields.EditableTextField
import ui.screens.BaseDetailsScreen
import ui.screens.master_detail.IDetailsComponent
import java.time.DayOfWeek
import java.time.LocalDateTime

@Composable
fun TaskDetailsUi(component: IDetailsComponent<Task>) {
    val task by remember(component) { component.item }.collectAsState(null)
    val user = LocalCurrentUser.current ?: return
    val navController = LocalNavController.current
    task?.let { t ->

        //only creator can edit the task
        RenderTaskDetails(
            t,
            isEditable = listOfNotNull(t.creator).contains(user),
            onTaskUpdated = { updatedTask ->
                component.updateItem(updatedTask)
            },
            onAddUsersClicked = {
                navController?.showUsersPickerDialog(
                    isMultipleSelection = true,
                    initialSelection = t.users,
                    onUsersPicked = {
                        component.updateItem(t.copy(users = it))
                    },
                    hiddenUsers = listOfNotNull(t.creator)
                )
            }
        )
    }

}


// https://dribbble.com/shots/5541961-Projecto-Desktop-Task-Management-App
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderTaskDetails(
    task: Task,
    isEditable: Boolean,
    onTaskUpdated: (Task) -> Unit,
    onAddUsersClicked: () -> Unit,
) {
    var tempTask by remember(task) { mutableStateOf(task) }

    var showDatePicker by remember { mutableStateOf(false) }

    var showForum by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val navController = LocalNavController.current

    BaseDetailsScreen(
        mainTag = tempTask.project?.let { project ->
            {
                Chip(
                    shape = UiSettings.DetailsScreen.tagsShape,
                    onClick = {
                        if (isEditable)
                            navController?.showProjectsPickerDialog(
                                initialSelection = listOfNotNull(project),
                                isMultipleSelection = false,
                                onProjectsPicked = {
                                    tempTask = tempTask.copy(project = it.firstOrNull())
                                }
                            )
                    },
                    content = { Text(text = project.name) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = project.color?.let { Color(it) }
                            ?: Color.Unspecified
                    ),
                    leadingIcon = if (project.icon.isNotEmpty()) {
                        {
                            Icon(painter = painterResource(project.icon), contentDescription = "иконка проекта")
                        }
                    } else null


                )
            }
        } ?: {
            if (isEditable) {
                TextButton(onClick = {
                    navController?.showProjectsPickerDialog(
                        initialSelection = listOf(),
                        isMultipleSelection = false,
                        onProjectsPicked = {
                            tempTask = tempTask.copy(project = it.firstOrNull())
                        }
                    )
                }, content = { Text("привязать к проекту") })
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
            RenderSubTasks(
                subtasks = tempTask.subtasks,
                isEditable = isEditable,
                onSubtasksEdited = {
                    tempTask = tempTask.copy(subtasks = it)
                }
            )
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
                label = if (tempTask.name.isEmpty()) "имя задачи" else "",
                withClearIcon = false
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
                RenderUserListItem(
                    user = user,
                    isCreator = task.creator?.id == user.id
                )

            }
            CircleIconButton(
                iconRes = "vector/add_circle_black_24dp.svg",
                onClick = {
                    //add user to the task
                    onAddUsersClicked()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            BadgedBox(
                modifier = Modifier.padding(12.dp),
                badge = {
                    Badge(
                        backgroundColor = MaterialTheme.colors.background
                    ) {
                        //if there are new messages - show badge here
                    }
                }) {
                IconButton(onClick = {
                    //open chat screen
                    showForum = !showForum
                }) {
                    Icon(
                        painterResource(resourcePath = "vector/forum_black_24dp.svg"),
                        contentDescription = "open chat screen",
                        tint = LocalContentColor.current.copy(alpha = if (showForum) ContentAlpha.high else ContentAlpha.medium)
                    )
                }
            }
        },
        bottomSheetContent = {
            Text(modifier = Modifier.padding(8.dp), text = "обсуждение:", style = MaterialTheme.typography.h6)
            RenderForum(task = tempTask, onMessageAdded = { message ->
                tempTask = tempTask.copy(messages = tempTask.messages.plus(message))
            })
        },
        bottomSheetState = bottomSheetState
    )

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

    LaunchedEffect(showForum) {
        if (showForum) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(tempTask) {
        if (tempTask == task)
            return@LaunchedEffect

        delay(UiSettings.Debounce.debounceTime)
        onTaskUpdated(tempTask)
    }
}

@Composable
private fun RenderUserListItem(user: User, isCreator: Boolean = false) {
    BadgedBox(
        modifier = Modifier.padding(12.dp),
        badge = {
            Badge(
                backgroundColor = MaterialTheme.colors.background
            ) {
                if (isCreator) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "task creator"
                    )
                }
            }
        }) {
        IconButton(onClick = {

        }) {
            Text(text = user.getFirstLetters(), style = MaterialTheme.typography.caption)
        }
    }

}

@Composable
private fun ColumnScope.RenderForum(task: Task, onMessageAdded: (TaskMessage) -> Unit) {
    if (task.messages.isEmpty()) {
        Card(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)) {
            Text(
                modifier = Modifier.padding(32.dp),
                text = "сообщений пока нет",
                style = MaterialTheme.typography.caption
            )
        }
    }
    task.messages.forEach {
        RenderMessage(it)
    }
    val currentUser = LocalCurrentUser.current
    currentUser?.let { user ->

        var tempMessage by remember { mutableStateOf("") }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RenderUserIcon(user = user)
            EditableTextField(
                modifier = Modifier.weight(1f),
                value = tempMessage,
                onValueChange = { tempMessage = it },
                label = if (tempMessage.isEmpty()) "введите комментарий..." else "",
                withClearIcon = false
            )
            IconButton(onClick = {
                onMessageAdded(
                    TaskMessage(text = tempMessage, user = user, createdAt = LocalDateTime.now())
                )
            }, enabled = tempMessage.isNotEmpty()) {
                Icon(Icons.Rounded.Send, contentDescription = "отправить сообщение")
            }
        }
    }
}

@Composable
private fun ColumnScope.RenderMessage(taskMessage: TaskMessage) {

    taskMessage.user?.let { user ->
        val currentUser = LocalCurrentUser.current
        CompositionLocalProvider(LocalLayoutDirection provides if (user.id == currentUser?.id) LayoutDirection.Ltr else LayoutDirection.Rtl) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RenderUserIcon(user = user)
                Text(modifier = Modifier.padding(8.dp), text = taskMessage.text)
            }
        }
    }

}

@Composable
private fun RenderUserIcon(user: User, onClick: (() -> Unit)? = null) {
    IconButton(modifier = Modifier.padding(8.dp), onClick = { onClick?.invoke() }) {
        Text(text = user.getFirstLetters(), style = MaterialTheme.typography.caption)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.RenderAttachments(
    attachments: List<Attachment>,
    isEditable: Boolean,
    onAttachmentsEdited: (List<Attachment>) -> Unit,
) {
    var editAttachmentIndex by remember { mutableStateOf(-1) }
    var addAttachment by remember { mutableStateOf(false) }
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(UiSettings.DetailsScreen.chipsHorizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(UiSettings.DetailsScreen.chipsVerticalSpacing)
    ) {
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


    if (isEditable)
        TextButton(onClick = { addAttachment = true }, content = { Text("добавить вложение") })


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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.RenderSubTasks(
    subtasks: List<SubTask>,
    isEditable: Boolean,
    onSubtasksEdited: (List<SubTask>) -> Unit,
) {

    var addSubtask by remember { mutableStateOf(false) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(UiSettings.DetailsScreen.chipsHorizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(UiSettings.DetailsScreen.chipsVerticalSpacing)
    ) {
        subtasks.forEachIndexed { index, subtask ->
            val currentUser = LocalCurrentUser.current
            RenderSubTask(subtask = subtask, onClick = {
                val editedCompleteTime = if (subtask.completedAt == null) {
                    LocalDateTime.now()
                } else null
                val editedUser = if (editedCompleteTime == null) {
                    null
                } else currentUser
                onSubtasksEdited(subtasks.toMutableList().apply {
                    this[index] = subtask.copy(completedAt = editedCompleteTime, completedBy = editedUser)
                })
            })
        }
    }

    if (isEditable)
        TextButton(onClick = { addSubtask = true }, content = { Text("добавить подзадачу") })

    if (addSubtask) {
        RenderTextInputDialog(
            title = "добавить подзадачу",
            hint = "описание",
            onDismiss = {
                addSubtask = false
            },
            onTextEdited = { text ->
                onSubtasksEdited(
                    subtasks.plus(SubTask(name = text, createdAt = LocalDateTime.now()))
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderSubTask(subtask: SubTask, onClick: () -> Unit) {
    Chip(
        onClick = onClick,
        leadingIcon = {
            val iconRes = remember(subtask) {
                if (subtask.completedAt == null) {
                    "vector/radio_button_unchecked_black_24dp.svg"
                } else "vector/check_circle_black_24dp.svg"
            }
            Icon(painter = painterResource(iconRes), contentDescription = null)
        }
    ) {
        Text(subtask.name)
    }
}


