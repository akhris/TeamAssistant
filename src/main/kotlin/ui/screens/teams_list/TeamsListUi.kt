package ui.screens.teams_list

import LocalCurrentUser
import androidx.compose.runtime.*
import domain.EntitiesList
import domain.Team
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode
import ui.screens.master_detail.IMasterComponent


@Composable
fun TeamsListUi(component: IMasterComponent<Team>) {

//    val dialogSlot by remember(teamsListComponent) { teamsListComponent.dialogSlot }.subscribeAsState()

    val teams by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    val user = LocalCurrentUser.current

    EntitiesListUi(
        teams,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<Team> {
            override fun getPrimaryText(item: Team) = item.name

            override fun getSecondaryText(item: Team) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Team) = ""

        },
        onItemClicked = {component.onItemClicked(it)}
    )
}