package ui.screens.root_ui

import domain.Project
import domain.Team
import domain.User

interface INavController {
    fun showUsersPickerDialog(
        isMultipleSelection: Boolean = false,
        initialSelection: List<User> = listOf(),
        onUsersPicked: (List<User>) -> Unit,
    )

    fun showTeamsPickerDialog(
        isMultipleSelection: Boolean = false,
        initialSelection: List<Team> = listOf(),
        onTeamsPicked: (List<Team>) -> Unit,
    )
    fun showProjectsPickerDialog(
        isMultipleSelection: Boolean = false,
        initialSelection: List<Project> = listOf(),
        onProjectsPicked: (List<Project>) -> Unit,
    )


}