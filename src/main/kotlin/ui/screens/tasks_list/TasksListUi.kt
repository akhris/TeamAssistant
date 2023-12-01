package ui.screens.tasks_list

import LocalCurrentUser
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.EntitiesList
import domain.Task
import tests.testTask1
import ui.FABState
import ui.IFABController
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.TextInputDialogUi

@Composable
fun TasksListUi(tasksListComponent: ITasksListComponent, fabController: IFABController) {
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

// https://dribbble.com/shots/6646573-To-do-list
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderTask(task: Task) {
    Card {
        ListItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, Color.White, CircleShape)
                        .padding(1.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                )
            },
            text = {
                Text(task.name)
            },
            secondaryText = task.creator?.let { creator ->
                {
                    Text(creator.getInitials())

                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewTasksUi() {
    RenderTask(testTask1)
}