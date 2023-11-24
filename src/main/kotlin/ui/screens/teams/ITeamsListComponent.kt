package ui.screens.teams

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import domain.Team
import ui.dialogs.IDialogComponent

interface ITeamsListComponent {

    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>
    /**
     * teams available for user
     * (created by user or with user as a member)
     */
    val teams: Value<List<Team>>

    /**
     * create new team
     */
    fun createNewTeam()

    /**
     * delete the team (if user is a team creator)
     */
    fun deleteTeam()


}

