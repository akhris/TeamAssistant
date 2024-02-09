package ui.screens.logged_in_root

import domain.Project
import domain.Team
import domain.User

interface INavController {
    fun showUsersPickerDialog(
        isMultipleSelection: Boolean = false,
        initialSelection: List<User> = listOf(),
        onUsersPicked: (List<User>) -> Unit,
        hiddenUsers: List<User> = listOf(),
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