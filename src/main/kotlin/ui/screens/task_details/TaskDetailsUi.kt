package ui.screens.task_details

import LocalCurrentUser
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import domain.SubTask
import domain.Task
import domain.valueobjects.Attachment
import ui.fields.DateTimeChip
import ui.fields.EditableTextField
import ui.fields.TitledCard
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
private fun RenderTaskDetails(task: Task, isEditable: Boolean, onTaskUpdated: (Task) -> Unit) {
    var tempTask by remember(task) { mutableStateOf(task) }

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

                    DateTimeChip(
                        dateTime = tempTask.targetDate,
                        label = "срок выполнения",
                        isEditable = isEditable,
                        onDateTimeChanged = { tempTask = tempTask.copy(targetDate = it) })

                }
            }
        }

        //description card:
        if (isEditable || tempTask.description.isNotEmpty()) {
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

        //users card:
        item {
            TitledCard(title={ Text("участники") }) {
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

        //attachments card:
        item {
            TitledCard(title={ Text("вложения") }) {
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


