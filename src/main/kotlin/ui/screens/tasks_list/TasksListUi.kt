package ui.screens.tasks_list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import domain.Task
import ui.screens.master_detail.IMasterComponent

@Composable
fun TasksListUi(component: IMasterComponent<Task>) {

    val tasks by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    val scrollstate = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(state = scrollstate),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (val tasksList = tasks) {
            is EntitiesList.Grouped -> TODO()
            is EntitiesList.NotGrouped -> {
                tasksList.items.forEach { task ->
                    RenderTask(task, onTaskClicked = {
                        component.onItemClicked(task)
                    })
                }
            }
        }

    }

}

// https://dribbble.com/shots/6646573-To-do-list
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderTask(task: Task, onTaskClicked: () -> Unit) {
    Card(modifier = Modifier.clickable {
        onTaskClicked()
    }) {
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

