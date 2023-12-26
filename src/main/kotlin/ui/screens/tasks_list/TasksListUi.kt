package ui.screens.tasks_list

import LocalCurrentUser
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
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode
import ui.dialogs.text_input_dialog.RenderTextInputDialog
import ui.screens.master_detail.IMasterComponent
import java.time.LocalDateTime

@Composable
fun TasksListUi(component: IMasterComponent<Task>) {

    val tasks by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    val scrollstate = rememberScrollState()

    var showAddNewTaskDialog by remember { mutableStateOf(false) }

    val currentUser = LocalCurrentUser.current

    EntitiesListUi(
        tasks,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<Task> {
            override fun getPrimaryText(item: Task) = item.name

            override fun getSecondaryText(item: Task) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Task) = null

            override fun getIconPath(item: Task): String = "vector/circle_black_24dp.svg"

            override fun getIconTint(item: Task): Color? = item.project?.color?.let { Color(it) }

        },
        onAddItemClick = {
            showAddNewTaskDialog = true
        },
        onItemClicked = {component.onItemClicked(it)}
    )

    if (showAddNewTaskDialog) {
        RenderTextInputDialog(
            title = "новая задача",
            hint = "название задачи",
            initialValue = "",
            onTextEdited = { text ->
                if (text.isNotEmpty()) {
                    component.onAddNewItem(
                        item = Task(
                            creator = currentUser,
                            createdAt = LocalDateTime.now(),
                            name = text
                        )
                    )
                }
            },
            onDismiss = {
                showAddNewTaskDialog = false
            }
        )
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

