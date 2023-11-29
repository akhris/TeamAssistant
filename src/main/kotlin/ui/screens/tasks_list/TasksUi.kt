package ui.screens.tasks_list

import LocalCurrentUser
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.EntitiesList
import domain.Task
import domain.valueobjects.State
import ui.FABState
import ui.IFABController
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.TextInputDialogUi

@Composable
fun TasksUi(tasksListComponent: ITasksListComponent, fabController: IFABController) {
    val dialogSlot by remember(tasksListComponent) { tasksListComponent.dialogSlot }.subscribeAsState()

    val tasks by remember(tasksListComponent) { tasksListComponent.tasks }.collectAsState(EntitiesList.empty())
    val user = LocalCurrentUser.current

    val scrollstate = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(state = scrollstate),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (val tasksList = tasks) {
            is EntitiesList.Grouped -> TODO()
            is EntitiesList.NotGrouped -> {
                tasksList.items.forEach { task ->
                    RenderTask(task)
                }
            }
        }

    }

    dialogSlot.child?.instance?.also { comp ->
        when (comp) {
            is IDialogComponent.ITextInputDialogComponent -> {
                TextInputDialogUi(
                    component = comp,
                    onOkClicked = { taskName ->
                        tasksListComponent.createNewTask(taskName, user)
                    }
                )
            }
        }

    }


    LaunchedEffect(fabController) {
        fabController.setFABState(
            FABState.VISIBLE(
                iconPath = "vector/add_black_24dp.svg",
                text = "добавить задачу",
                description = "добавить новую задачу"
            )
        )
        fabController
            .clicks
            .collect {
                tasksListComponent.createNewTaskRequest()
            }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderTask(task: Task) {
    Card {
        ListItem(modifier = Modifier.padding(4.dp), text = {
            Text(text = task.description)
        }, overlineText = {
            Text(text = task.name)
        }, secondaryText = task.creator?.getInitials()?.let {
            {
                Text(text = it)
            }
        })
    }
}

@Preview
@Composable
private fun PreviewTasksUi() {
//    TasksUi(listOf(testTask1, testTask2))
}