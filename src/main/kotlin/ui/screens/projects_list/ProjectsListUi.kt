package ui.screens.projects_list

import androidx.compose.runtime.*
import domain.EntitiesList
import domain.Project
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode
import ui.dialogs.text_input_dialog.RenderTextInputDialog
import ui.screens.master_detail.IMasterComponent
import java.time.LocalDateTime

@Composable
fun ProjectsListUi(component: IMasterComponent<Project>) {
    val projects by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    var showAddNewProjectDialog by remember { mutableStateOf(false) }

    EntitiesListUi(
        projects,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<Project> {
            override fun getPrimaryText(item: Project) = item.name

            override fun getSecondaryText(item: Project) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Project) = null

            override fun getIconPath(item: Project): String =
                item.icon.ifEmpty { "vector/projects/rocket_black_24dp.svg" }

        },
        onItemClicked = { component.onItemClicked(it) },
        onAddItemClick = {
            showAddNewProjectDialog = true
        }
    )

    if (showAddNewProjectDialog) {
        RenderTextInputDialog(
            title = "новый проект",
            hint = "название проекта",
            initialValue = "",
            onTextEdited = { text ->
                if (text.isNotEmpty()) {
                    component.onAddNewItem(
                        item = Project(
                            creator = component.currentUser,
                            createdAt = LocalDateTime.now(),
                            name = text
                        )
                    )
                }
            },
            onDismiss = {
                showAddNewProjectDialog = false
            }
        )
    }

}
