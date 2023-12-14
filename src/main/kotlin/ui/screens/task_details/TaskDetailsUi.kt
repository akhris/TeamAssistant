package ui.screens.task_details

import LocalCurrentUser
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.SubTask
import domain.Task
import domain.valueobjects.Attachment
import ui.dialogs.DatePickerDialog
import ui.fields.CircleIconButton
import ui.fields.EditableTextField
import ui.screens.BaseDetailsScreen
import ui.screens.master_detail.IDetailsComponent

@Composable
fun TaskDetailsUi(component: IDetailsComponent<Task>) {
    val task by remember(component) { component.item }.collectAsState(null)
    val user = LocalCurrentUser.current ?: return
    task?.let {

        //only creator can edit the task
        RenderTaskDetails(
            it,
            isEditable = listOfNotNull(it.creator).contains(user),
            onTaskUpdated = {

            }
        )

    }
}


// https://dribbble.com/shots/5541961-Projecto-Desktop-Task-Management-App
@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
private fun RenderTaskDetails(task: Task, isEditable: Boolean, onTaskUpdated: (Task) -> Unit) {
    var tempTask by remember(task) { mutableStateOf(task) }

    var showDatePicker by remember { mutableStateOf(false) }

    BaseDetailsScreen(
        mainTag = tempTask.project?.let { p ->
            {
                Chip(
                    shape = MaterialTheme.shapes.small,
                    onClick = {},
                    content = { Text(p.name) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = p.color?.let { Color(it) }
                            ?: MaterialTheme.colors.onSurface
                    )
                )
            }
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
        description = {
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
        },
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
            }

        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderAttachment(attachment: Attachment, onClick: () -> Unit) {
    // TODO: add description tooltip
    Chip(
        onClick = onClick,
        content = {
            Text(attachment.name)
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


