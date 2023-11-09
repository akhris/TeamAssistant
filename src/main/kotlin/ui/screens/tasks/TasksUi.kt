package ui.screens.tasks

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Task
import domain.valueobjects.State
import tests.testTask1
import tests.testTask2
import ui.UiSettings

@Composable
fun TasksUi(tasks: List<Task>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        tasks.forEach {
            RenderTask(it)
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
        }, secondaryText = task.state?.let {
            {
                when (it) {
                    State.Task.ACTIVE -> {
                        Text("active")
                    }

                    State.Task.COMPLETED -> {
                        Text("completed")
                    }

                    is State.Task.Failed -> {
                        Text("failed")
                    }

                    is State.Task.InProgress -> {
                        Text(String.format("%d%% progress", (it.progress * 100).toInt()))
                    }
                }
            }
        })
    }
}

@Preview
@Composable
private fun PreviewTasksUi() {
    TasksUi(listOf(testTask1, testTask2))
}