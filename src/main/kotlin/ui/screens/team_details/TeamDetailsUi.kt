package ui.screens.team_details

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import domain.Team
import ui.screens.master_detail.IDetailsComponent

@Composable
fun TeamDetailsUi(component: IDetailsComponent<Team>) {
    val team by remember(component) { component.item }.collectAsState(null)
    team?.let {
        Text("team details for $it")
    }
}