package ui.screens.teams_list

import domain.Team
import domain.User
import ui.screens.master_detail.IMasterComponent

interface ITeamsListComponent : IMasterComponent<Team> {

//    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>
    /**
     * teams available for user
     * (created by user or with user as a member)
     */
//    val teams: Flow<EntitiesList<Team>>

    /**
     * Show a dialog for creating new team
     */
    fun createNewTeamRequest()

    /**
     * Create new team and store it in database
     */
    fun createNewTeam(name: String, creator: User?)

    /**
     * delete the team (if user is a team creator)
     */
    fun deleteTeam()


}

