package ui.screens.teams_list

import LocalCurrentUser
import androidx.compose.runtime.*
import domain.EntitiesList
import domain.Project
import domain.Team
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode
import ui.dialogs.text_input_dialog.RenderTextInputDialog
import ui.screens.master_detail.IMasterComponent
import java.time.LocalDateTime


@Composable
fun TeamsListUi(component: IMasterComponent<Team>) {

//    val dialogSlot by remember(teamsListComponent) { teamsListComponent.dialogSlot }.subscribeAsState()

    val teams by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    var showAddNewTeamDialog by remember { mutableStateOf(false) }

    val currentUser = LocalCurrentUser.current

    EntitiesListUi(
        teams,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<Team> {
            override fun getPrimaryText(item: Team) = item.name

            override fun getSecondaryText(item: Team) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Team) = ""

        },
        onItemClicked = {component.onItemClicked(it)},
        onAddItemClick = {
            showAddNewTeamDialog = true
        }
    )

    if (showAddNewTeamDialog) {
        RenderTextInputDialog(
            title = "новая команда",
            hint = "название команды",
            initialValue = "",
            onTextEdited = { text ->
                if (text.isNotEmpty()) {
                    component.onAddNewItem(
                        item = Team(
                            creator = currentUser,
                            createdAt = LocalDateTime.now(),
                            name = text
                        )
                    )
                }
            },
            onDismiss = {
                showAddNewTeamDialog = false
            }
        )
    }
}