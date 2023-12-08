package ui.screens.teams_list

import LocalCurrentUser
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import domain.Team
import domain.User
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectableMode
import ui.screens.master_detail.IMasterComponent


@Composable
fun TeamsListUi(component: IMasterComponent<Team>) {

//    val dialogSlot by remember(teamsListComponent) { teamsListComponent.dialogSlot }.subscribeAsState()

    val teams by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    val user = LocalCurrentUser.current

    EntitiesListUi(
        teams,
        selectableMode = SelectableMode.NonSelectable(onItemClicked = {
            component.onItemClicked(it)
        }),
        itemRenderer = object : ItemRenderer<Team> {
            override fun getPrimaryText(item: Team) = item.name

            override fun getSecondaryText(item: Team) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Team) = ""

        }
    )
}