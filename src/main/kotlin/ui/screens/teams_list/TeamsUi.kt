package ui.screens.teams_list

import LocalCurrentUser
import androidx.compose.desktop.ui.tooling.preview.Preview
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
fun TeamsUi(teamsListComponent: ITeamsListComponent, fabController: IFABController) {

    val dialogSlot by remember(teamsListComponent) { teamsListComponent.dialogSlot }.subscribeAsState()

    val teams by remember(teamsListComponent) { teamsListComponent.teams }.collectAsState(EntitiesList.empty())

    val user = LocalCurrentUser.current

    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (val teamsList = teams) {
            is EntitiesList.Grouped -> TODO()
            is EntitiesList.NotGrouped -> {
                teamsList.items.forEach { team ->
                    ListItem(
                        text = { Text(team.name) },
                        secondaryText = team.creator?.let { { Text(it.getInitials()) } }
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
                    onOkClicked = { teamName ->
                        teamsListComponent.createNewTeam(teamName, user)
                    }
                )
            }
        }

    }


    LaunchedEffect(fabController) {
        fabController.setFABState(
            FABState.VISIBLE(
                iconPath = "vector/add_black_24dp.svg",
                text = "добавить команду",
                description = "добавить новую команду"
            )
        )
        fabController
            .clicks
            .collect {
                teamsListComponent.createNewTeamRequest()
            }
    }
}

@Preview
@Composable
fun userDetailsPreview() {
//    TeamsUi(testTeam1)
}