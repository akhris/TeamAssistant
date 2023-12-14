package ui.screens.project_details

import LocalCurrentUser
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.onClick
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.Project
import ui.dialogs.IconsPickerDialog
import ui.fields.EditableTextField
import ui.fields.TitledCard
import ui.screens.master_detail.IDetailsComponent
import utils.ResourcesUtils

@Composable
fun ProjectDetailsUi(component: IDetailsComponent<Project>) {
    val project by remember(component) { component.item }.collectAsState(null)
    val user = LocalCurrentUser.current ?: return

    project?.let {
        RenderProjectDetails(
            project = it,
            isEditable = listOfNotNull(it.creator).contains(user),
            onProjectUpdated = {}
        )
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun RenderProjectDetails(project: Project, isEditable: Boolean, onProjectUpdated: (Project) -> Unit) {

    var tempProject by remember(project) { mutableStateOf(project) }

    var showIconPicker by remember { mutableStateOf(false) }

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(300.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        //main card
        item {
            TitledCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EditableTextField(
                        modifier = Modifier.weight(1f),
                        value = tempProject.name,
                        isEditable = isEditable,
                        textStyle = MaterialTheme.typography.h4,
                        onValueChange = {
                            tempProject = tempProject.copy(name = it)
                        },
                        label = if (tempProject.name.isEmpty()) "имя проекта" else ""
                    )
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .onClick {
                                showIconPicker = true
                            },
                        painter = painterResource(tempProject.icon.ifEmpty { "vector/broken_image_black_24dp.svg" }),
                        contentDescription = "иконка проекта"
                    )
                }
            }
        }

        item {
            TitledCard(title = { Text("команды") }) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tempProject.teams.forEach { team ->
                        Chip(onClick = {}, content = {
                            Text(team.name)
                        }, leadingIcon = {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = "удалить команду из проекта"
                            )
                        })
                    }
                    Chip(onClick = {}) {
                        Icon(
                            painterResource("vector/group_add_black_24dp.svg"),
                            contentDescription = "добавить команду в проект"
                        )
                    }
                }
            }
        }
    }

    if (showIconPicker) {
        IconsPickerDialog(
            iconsFolder = ResourcesUtils.ResourcesFolder.ProjectIcons,
            initialSelection = tempProject.icon,
            onIconPicked = { tempProject = tempProject.copy(icon = it) },
            onDismiss = { showIconPicker = false }
        )
    }
}