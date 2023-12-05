package ui.screens.projects_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import domain.Project
import ui.screens.master_detail.IMasterComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectsListUi(projectsListComponent: IMasterComponent<Project>) {


    val projects by remember(projectsListComponent) { projectsListComponent.items }.collectAsState(EntitiesList.empty())


    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (val projectsList = projects) {
            is EntitiesList.Grouped -> TODO()
            is EntitiesList.NotGrouped -> {
                projectsList.items.forEach { project ->
                    RenderProject(project) {
                        projectsListComponent.onItemClicked(project)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderProject(project: Project, onProjectClicked: () -> Unit) {
    Card(modifier = Modifier.clickable { onProjectClicked() }) {
        ListItem(modifier = Modifier.padding(4.dp), text = {
            Text(text = project.description)
        }, overlineText = {
            Text(text = project.name)
        }, secondaryText = project.creator?.getInitials()?.let {
            {
                Text(text = it)
            }
        })
    }
}