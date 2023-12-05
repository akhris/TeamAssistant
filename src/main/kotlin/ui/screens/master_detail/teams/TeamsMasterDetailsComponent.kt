package ui.screens.master_detail.teams

import com.arkivanov.decompose.ComponentContext
import domain.Team
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.team_details.TeamDetailsComponent
import ui.screens.teams_list.TeamsListComponent

class TeamsMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<Team>(componentContext,
        createMasterComponent = { componentContext: ComponentContext, onItemSelected: (String) -> Unit ->
            TeamsListComponent(di = di, componentContext = componentContext, onItemSelected = onItemSelected)
        },
        createDetailsComponent = {componentContext, itemID->
            TeamDetailsComponent(di = di, componentContext = componentContext, teamID = itemID)
        }
        ) {

}