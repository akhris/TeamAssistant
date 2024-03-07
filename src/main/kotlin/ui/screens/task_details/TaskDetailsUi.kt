package ui.screens.task_details

import LocalNavController
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Star
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
import domain.SubCheck
import domain.Task
import domain.User
import domain.valueobjects.Attachment
import domain.valueobjects.TaskMessage
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.dialogs.DatePickerDialog
import ui.dialogs.EditAttachmentDialog
import ui.dialogs.text_input_dialog.RenderTextInputDialog
import ui.fields.CircleIconButton
import ui.fields.DateTimeChip
import ui.fields.EditableTextField
import ui.screens.BaseDetailsScreen
import ui.screens.master_detail.IDetailsComponent
import utils.log
import utils.oppositeColor
import java.time.DayOfWeek
import java.time.LocalDateTime

@Composable
fun TaskDetailsUi(component: IDetailsComponent<Task>) {
    val task by remember(component) { component.item }.collectAsState(null)

    val navController = LocalNavController.current
    task?.let { t ->

        //only creator can edit the task
        RenderTaskDetails(
            t,
            currentUser = component.currentUser,
            isEditable = setOfNotNull(t.creator).contains(component.currentUser),
            isControllable = setOfNotNull(t.creator).plus(t.users).contains(component.currentUser),
            onTaskUpdated = { updatedTask ->
                try {
                    component.updateItem(updatedTask)
                } catch (e: Exception) {
                    log(e.localizedMessage, "error while updating task:")
                    // TODO: show error dialog using navController
                }
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


/**
 * Rendering task details function.
 * @param isEditable - true for task admin
 * @param isControllable - true for task admin or task members
 *
 * design inspired partially on:
 * https://dribbble.com/shots/5541961-Projecto-Desktop-Task-Management-App
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderTaskDetails(
    task: Task,
    currentUser: User,
    isEditable: Boolean,
    isControllable: Boolean,
    onTaskUpdated: (Task) -> Unit,
    onAddUsersClicked: () -> Unit,
) {
    var tempTask by remember(task) { mutableStateOf(task) }

    var showDatePicker by remember { mutableStateOf(false) }

    var showForum by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val navController = LocalNavController.current

    BaseDetailsScreen(
        mainTag = {
            RenderMainTag(
                task = tempTask,
                isEditable = isEditable,
                onTaskUpdated = {
                    tempTask = it
                }
            )
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
                        shape = UiSettings.MasterDetailsScreen.tagsShape,
                        onDateTimeChanged = { tempTask = tempTask.copy(targetDate = it) })
            }
        } else null,
        body = {
            RenderAttachments(
                attachments = tempTask.attachments,
                isEditable = isEditable,
                onAttachmentsEdited = {
                    tempTask = tempTask.copy(attachments = it)
                })
            RenderSubTasks(
                subtasks = tempTask.subchecks,
                currentUser = currentUser,
                isEditable = isEditable,
                isControllable = isControllable,
                onSubtasksEdited = {
                    tempTask = tempTask.copy(subchecks = it)
                }
            )
            RenderActions(task = tempTask, isEditable = isEditable, onTaskUpdated = onTaskUpdated)
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
            task.creator?.let { creator ->
                Text(modifier = Modifier.padding(4.dp), text = "создатель", style = MaterialTheme.typography.caption)
                RenderUserListItem(
                    user = creator,
                    isCreator = true
                )
            }
            if (task.users.isNotEmpty()) {
                Text(modifier = Modifier.padding(4.dp), text = "участники", style = MaterialTheme.typography.caption)
                (task.users).forEach { user ->
                    RenderUserListItem(
                        user = user,
                        isCreator = task.creator?.id == user.id
                    )
                }
            }
            if (isEditable)
                CircleIconButton(
                    iconRes = "vector/add_circle_black_24dp.svg",
                    onClick = {
                        //add user to the task
                        onAddUsersClicked()
                    }
                )
            Spacer(modifier = Modifier.weight(1f))
            /*
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
            */

        },
//        bottomSheetContent = {
//            Text(modifier = Modifier.padding(8.dp), text = "обсуждение:", style = MaterialTheme.typography.h6)
//            RenderForum(task = tempTask, currentUser = currentUser, onMessageAdded = { message ->
//                tempTask = tempTask.copy(messages = tempTask.messages.plus(message))
//            })
//        },
//        bottomSheetState = bottomSheetState
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderMainTag(task: Task, isEditable: Boolean, onTaskUpdated: (Task) -> Unit) {
    val navController = LocalNavController.current

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (task.parentTask == null) {
            //show parent project only for top-level task
            task.project?.let { project ->
                Chip(
                    shape = UiSettings.MasterDetailsScreen.tagsShape,
                    onClick = {
                        if (isEditable)
                            navController?.showProjectsPickerDialog(
                                initialSelection = listOfNotNull(project),
                                isMultipleSelection = false,
                                onProjectsPicked = {
                                    onTaskUpdated(task.copy(project = it.firstOrNull()))
                                }
                            )
                    },
                    content = { Text(text = project.name) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = project.color?.let { Color(it) }
                            ?: Color.Unspecified,
                        contentColor = project.color?.let { Color(it) }?.oppositeColor()
                            ?: MaterialTheme.colors.onSurface.copy(alpha = ChipDefaults.ContentOpacity)
                    ),
                    leadingIcon = if (project.icon.isNotEmpty()) {
                        {
                            Icon(
                                painter = painterResource(project.icon),
                                contentDescription = "иконка проекта",
                                tint = project.color?.let { Color(it) }?.oppositeColor()
                                    ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                            )
                        }
                    } else null


                )
            }
        }

        task.parentTask?.let { parentTask ->
            Chip(
                shape = UiSettings.MasterDetailsScreen.tagsShape,
                onClick = {
                    if (isEditable)
                        navController?.showTasksPickerDialog(
                            initialSelection = listOfNotNull(parentTask),
                            isMultipleSelection = false,
                            hiddenTasks = listOf(task),
                            onTasksPicked = {
                                onTaskUpdated(task.copy(parentTask = it.firstOrNull()))
                            }
                        )
                },
                content = { Text(text = parentTask.name) },
//                colors = ChipDefaults.chipColors(
//                    backgroundColor = parentTask.color?.let { Color(it) }
//                        ?: Color.Unspecified
//                ),

            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderUserListItem(user: User, isCreator: Boolean = false) {
    Chip(onClick = {

    }) {
        Text(text = user.getInitials())
    }
    /*
    BadgedBox(
        modifier = Modifier.padding(end = 12.dp),
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
        }
    ) {

    }


     */
}

@Composable
private fun ColumnScope.RenderForum(task: Task, currentUser: User, onMessageAdded: (TaskMessage) -> Unit) {
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
        RenderMessage(it, currentUser)
    }


    var tempMessage by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        RenderUserIcon(user = currentUser)
        EditableTextField(
            modifier = Modifier.weight(1f),
            value = tempMessage,
            onValueChange = { tempMessage = it },
            label = if (tempMessage.isEmpty()) "введите комментарий..." else "",
            withClearIcon = false
        )
        IconButton(onClick = {
            onMessageAdded(
                TaskMessage(text = tempMessage, user = currentUser, createdAt = LocalDateTime.now())
            )
        }, enabled = tempMessage.isNotEmpty()) {
            Icon(Icons.Rounded.Send, contentDescription = "отправить сообщение")
        }
    }

}

@Composable
private fun ColumnScope.RenderMessage(taskMessage: TaskMessage, currentUser: User) {

    taskMessage.user?.let { user ->
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
        horizontalArrangement = Arrangement.spacedBy(UiSettings.MasterDetailsScreen.chipsHorizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(UiSettings.MasterDetailsScreen.chipsVerticalSpacing)
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

@Composable
private fun ColumnScope.RenderActions(task: Task, isEditable: Boolean, onTaskUpdated: (Task) -> Unit) {
    val navController = LocalNavController.current

    if (isEditable && task.project == null) {
        if (task.parentTask == null) {
            TextButton(onClick = {
                navController?.showTasksPickerDialog(
                    initialSelection = listOf(),
                    isMultipleSelection = false,
                    hiddenTasks = listOf(task),
                    onTasksPicked = {
                        onTaskUpdated(task.copy(parentTask = it.firstOrNull()))
                    }
                )
            }, content = { Text("выбрать родительскую задачу") })
        } else {
            TextButton(onClick = {
                onTaskUpdated(task.copy(parentTask = null))
            }, content = { Text("убрать родительскую задачу") })
        }
    }
    if (isEditable && task.parentTask == null) {
        if (task.project == null) {
            TextButton(onClick = {
                navController?.showProjectsPickerDialog(
                    initialSelection = listOf(),
                    isMultipleSelection = false,
                    onProjectsPicked = {
                        onTaskUpdated(task.copy(project = it.firstOrNull()))
                    }
                )
            }, content = { Text("привязать к проекту") })
        } else {
            TextButton(onClick = {
                onTaskUpdated(task.copy(project = null))
            }, content = { Text("отвязать от проекта") })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.RenderSubTasks(
    subtasks: List<SubCheck>,
    currentUser: User,
    isEditable: Boolean,
    isControllable: Boolean,
    onSubtasksEdited: (List<SubCheck>) -> Unit,
) {

    var addSubtask by remember { mutableStateOf(false) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(UiSettings.MasterDetailsScreen.chipsHorizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(UiSettings.MasterDetailsScreen.chipsVerticalSpacing)
    ) {
        subtasks.forEachIndexed { index, subtask ->
            RenderSubTask(subtask = subtask, isControllable = isControllable, onClick = {
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
                    subtasks.plus(SubCheck(name = text, createdAt = LocalDateTime.now()))
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderSubTask(subtask: SubCheck, isControllable: Boolean, onClick: () -> Unit) {
    Chip(
        enabled = isControllable,
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


