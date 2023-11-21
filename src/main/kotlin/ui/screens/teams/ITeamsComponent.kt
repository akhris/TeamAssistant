package ui.screens.teams

import com.arkivanov.decompose.value.Value
import domain.Team

interface ITeamsComponent {
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
     * team creator can delete the team
     */
    fun deleteTeam()


}