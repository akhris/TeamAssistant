package ui.screens.teams_list

import LocalCurrentUser
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import domain.Team
import ui.screens.master_detail.IMasterComponent


@Composable
fun TeamsListUi(teamsListComponent: IMasterComponent<Team>) {

//    val dialogSlot by remember(teamsListComponent) { teamsListComponent.dialogSlot }.subscribeAsState()

    val teams by remember(teamsListComponent) { teamsListComponent.items }.collectAsState(EntitiesList.empty())

    val user = LocalCurrentUser.current

    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (val teamsList = teams) {
            is EntitiesList.Grouped -> TODO()
            is EntitiesList.NotGrouped -> {
                teamsList.items.forEach { team ->
                    RenderTeamItem(team) {
                        teamsListComponent.onItemClicked(team)
                    }
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderTeamItem(team: Team, onTeamClick: () -> Unit) {
    Card(modifier = Modifier.clickable { onTeamClick() }) {
        ListItem(modifier = Modifier.padding(4.dp), text = {
            Text(text = team.name)
        }, secondaryText = team.creator?.getInitials()?.let {
            {
                Text(text = it)
            }
        })
    }
}

@Preview
@Composable
fun userDetailsPreview() {
//    TeamsUi(testTeam1)
}