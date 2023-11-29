package ui.screens.root_ui

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.User
import kotlinx.coroutines.flow.Flow
import ui.NavItem
import ui.screens.projects_list.IProjectsListComponent
import ui.screens.tasks_list.ITasksListComponent
import ui.screens.teams_list.ITeamsListComponent
import ui.screens.user_details.IUserDetailsComponent

interface IRootComponent {

    val navHostStack: Value<ChildStack<*, NavHost>>
    val dialogStack: Value<ChildStack<*, Dialog>>
//    val toolbarUtilsStack: Value<ChildStack<*, ToolbarUtils>>

    val userLoggingInfo: Flow<UserLoggingInfo>
    val currentDestination: Value<NavItem>

    fun createNewUser(user: User)
    fun navigateTo(navItem: NavItem)

    sealed class NavHost {
        class UserDetails(val component: IUserDetailsComponent) : NavHost()
        class Tasks(val component: ITasksListComponent) : NavHost()
        class Activity : NavHost()
        class Projects(val component: IProjectsListComponent) : NavHost()
        class Team(val component: ITeamsListComponent) : NavHost()
    }

    sealed class Dialog {
        object None : Dialog()
    }

    sealed class ToolbarUtils {
//        class SampleTypesSelector(val component: ISampleTypesSelector) : ToolbarUtils()
    }

    fun dismissDialog()

    data class UserLoggingInfo(
        val userID: String = "",
        val user: User? = null
    )
}