package ui.screens.task_details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.SubTask
import domain.Task
import domain.valueobjects.Attachment
import tests.testTask1

@Composable
fun TaskDetailsUi(component: ITaskDetailsComponent) {
    val task by remember(component) { component.task }.collectAsState(null)
    task?.let {
        RenderTaskDetails(it)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
private fun RenderTaskDetails(task: Task) {
    Card {
        Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            //name
            Text(text = task.name, style = MaterialTheme.typography.h4)


            Chip(onClick = {}) {
                Text("date and time")
            }

            if (task.users.isNotEmpty()) {
//                Text(text = "исполнители", style = MaterialTheme.typography.caption)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    task.users.forEach {
                        Chip(onClick = {}, content = {
                            Text(it.getInitials())
                        }, leadingIcon = {
                            Icon(painterResource("vector/person_black_24dp.svg"), contentDescription = "person icon")
                        })
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }

            //attachments
            if (task.attachments.isNotEmpty()) {
//                Text(text = "вложения", style = MaterialTheme.typography.caption)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    task.attachments.forEach {
                        RenderAttachment(it, onClick = {})
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }

            //description
            if (task.description.isNotEmpty()) {
//                Text(text = "описание", style = MaterialTheme.typography.caption)

                Text(text = task.description, style = MaterialTheme.typography.body1)
            }

            //subtasks
            if (task.subtasks.isNotEmpty()) {
                task.subtasks.forEach {
                    RenderSubTask(subTask = it, onClick = {})
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

@Preview
@Composable
private fun PreviewTasksUi() {
    RenderTaskDetails(testTask1)
}