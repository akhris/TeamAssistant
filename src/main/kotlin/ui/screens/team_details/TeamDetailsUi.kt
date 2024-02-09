package ui.screens.team_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.Team
import ui.fields.EditableTextField
import ui.fields.TitledCard
import ui.screens.master_detail.IDetailsComponent

@Composable
fun TeamDetailsUi(component: IDetailsComponent<Team>) {
    val team by remember(component) { component.item }.collectAsState(null)
    team?.let {
        RenderTeamDetails(
            team = it,
            isEditable = listOfNotNull(it.creator).plus(it.admins).contains(component.currentUser),
            onTeamUpdated = {}
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
private fun RenderTeamDetails(team: Team, isEditable: Boolean, onTeamUpdated: (Team) -> Unit) {
    var tempTeam by remember(team) { mutableStateOf(team) }

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(300.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        //main card
        item {
            TitledCard {
                EditableTextField(
                    value = tempTeam.name,
                    isEditable = isEditable,
                    textStyle = MaterialTheme.typography.h4,
                    onValueChange = {
                        tempTeam = tempTeam.copy(name = it)
                    },
                    label = if (tempTeam.name.isEmpty()) "имя команды" else ""
                )
            }
        }

        item {
            TitledCard(title = { Text("участники") }) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOfNotNull(tempTeam.creator).plus(tempTeam.admins).plus(tempTeam.members).toSet()
                        .forEach { user ->
                            Chip(onClick = {}, content = {
                                Text(user.getInitials())
                            }, leadingIcon = if ((user in tempTeam.admins) or (user.id == tempTeam.creator?.id)) {
                                {
                                    Icon(
                                        painterResource("vector/manage_accounts_black_24dp.svg"),
                                        contentDescription = "администратор команды"
                                    )
                                }
                            } else {
                                {
                                    Icon(
                                        painterResource("vector/users/person_black_24dp.svg"),
                                        contentDescription = "член команды"
                                    )
                                }
                            })
                        }

                    Chip(onClick = {}) {
                        Icon(
                            painterResource("vector/person_add_black_24dp.svg"),
                            contentDescription = "добавить пользователя к команде"
                        )
                    }
                }
            }
        }
    }
}
