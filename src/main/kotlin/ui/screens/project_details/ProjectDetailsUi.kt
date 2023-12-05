package ui.screens.project_details

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import domain.Project
import ui.screens.master_detail.IDetailsComponent

@Composable
fun ProjectDetailsUi(component: IDetailsComponent<Project>){
    val project by remember(component) { component.item }.collectAsState(null)
    project?.let {
        Text("project details for $project")
    }
}