package ui.screens.projects_list

import LocalCurrentUser
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.EntitiesList
import ui.FABState
import ui.IFABController
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.TextInputDialogUi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectsUi(projectsListComponent: IProjectsListComponent, fabController: IFABController) {

    val dialogSlot by remember(projectsListComponent) { projectsListComponent.dialogSlot }.subscribeAsState()

    val projects by remember(projectsListComponent) { projectsListComponent.projects }.collectAsState(EntitiesList.empty())

    val user = LocalCurrentUser.current

    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (val projectsList = projects) {
            is EntitiesList.Grouped -> TODO()
            is EntitiesList.NotGrouped -> {
                projectsList.items.forEach { project ->
                    ListItem(
                        text = { Text(project.name) },
                        secondaryText = project.creator?.let { { Text(it.getInitials()) } }
                    )
                }
            }
        }

    }


    dialogSlot.child?.instance?.also { comp ->
        when (comp) {
            is IDialogComponent.ITextInputDialogComponent -> {
                TextInputDialogUi(
                    component = comp,
                    onOkClicked = { projectName ->
                        projectsListComponent.createNewProject(projectName, user)
                    }
                )
            }
        }

    }


    LaunchedEffect(fabController) {
        fabController.setFABState(
            FABState.VISIBLE(
                iconPath = "vector/add_black_24dp.svg",
                text = "добавить проект",
                description = "добавить новый проект"
            )
        )
        fabController
            .clicks
            .collect {
                projectsListComponent.createNewProjectRequest()
            }
    }
}