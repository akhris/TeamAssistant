package ui.screens.project_details

import LocalNavController
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.Project
import domain.Team
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.dialogs.IconsPickerDialog
import ui.fields.CircleIconButton
import ui.fields.EditableTextField
import ui.screens.BaseDetailsScreen
import ui.screens.master_detail.IDetailsComponent
import utils.ResourcesUtils

@Composable
fun ProjectDetailsUi(component: IDetailsComponent<Project>) {
    val project by remember(component) { component.item }.collectAsState(null)

    project?.let {
        RenderProjectDetails(
            project = it,
            isEditable = listOfNotNull(it.creator).contains( component.currentUser),
            onProjectUpdated = { updatedProject ->
                component.updateItem(updatedProject)
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun RenderProjectDetails(project: Project, isEditable: Boolean, onProjectUpdated: (Project) -> Unit) {

    var tempProject by remember(project) { mutableStateOf(project) }

    var showIconPicker by remember { mutableStateOf(false) }
    var showForum by remember { mutableStateOf(false) }
    val navController = LocalNavController.current
    BaseDetailsScreen(
        title = {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(64.dp)
                        .clickable {
                            showIconPicker = true
                        }

                        ,
                    painter = painterResource(tempProject.icon.ifEmpty { "vector/broken_image_black_24dp.svg" }),
                    contentDescription = "иконка проекта"
                )
                EditableTextField(
                    modifier = Modifier.weight(1f),
                    value = tempProject.name,
                    isEditable = isEditable,
                    textStyle = MaterialTheme.typography.h4,
                    onValueChange = {
                        tempProject = tempProject.copy(name = it)
                    },
                    label = if (tempProject.name.isEmpty()) "имя проекта" else "",
                    withClearIcon = false
                )

            }
        },
        description =
        if (tempProject.description.isNotEmpty() || isEditable) {
            {
                EditableTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    value = tempProject.description,
                    onValueChange = {
                        tempProject = tempProject.copy(description = it)
                    },
                    textStyle = MaterialTheme.typography.body1,
                    label = if (tempProject.description.isEmpty()) "описание" else "",
                    isEditable = isEditable
                )
            }
        } else null,
        rightPanel = {
            Text(modifier = Modifier.padding(4.dp), text = "команды", style = MaterialTheme.typography.caption)

            tempProject.teams.forEach { team ->
                RenderTeamListIcon(
                    team = team
                )

            }
            CircleIconButton(
                iconRes = "vector/add_circle_black_24dp.svg",
                onClick = {
                    //add team to the project
                    navController?.showTeamsPickerDialog(
                        isMultipleSelection = true,
                        initialSelection = tempProject.teams,
                        onTeamsPicked = {
                            tempProject = tempProject.copy(teams = it)
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            BadgedBox(
                modifier = Modifier.padding(12.dp),
                badge = {
                    Badge(
                        backgroundColor = MaterialTheme.colors.background
                    ) {
                        //if there are new messages - show badge here
                    }
                }) {
                /*
                IconButton(onClick = {
                    //open chat screen
                    showForum = !showForum
                }) {
                    Icon(
                        painterResource(resourcePath = "vector/forum_black_24dp.svg"),
                        contentDescription = "open chat screen",
                        tint = LocalContentColor.current.copy(alpha = if (showForum) ContentAlpha.high else ContentAlpha.medium)
                    )
                }

                 */
            }
        }
    )


    if (showIconPicker) {
        IconsPickerDialog(
            iconsFolder = ResourcesUtils.ResourcesFolder.ProjectIcons,
            initialSelection = tempProject.icon,
            onIconPicked = { tempProject = tempProject.copy(icon = it) },
            onDismiss = { showIconPicker = false }
        )
    }

    LaunchedEffect(tempProject) {
        if (tempProject == project)
            return@LaunchedEffect

        delay(UiSettings.Debounce.debounceTime)
        onProjectUpdated(tempProject)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderTeamListIcon(team: Team) {

    Chip(onClick = {

    }) {
        Text(text = team.name)
    }
}