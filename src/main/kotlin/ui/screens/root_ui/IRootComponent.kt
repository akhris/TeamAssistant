package ui.screens.root_ui

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.Project
import domain.Task
import domain.Team
import domain.User
import kotlinx.coroutines.flow.Flow
import ui.NavItem
import ui.dialogs.IDialogComponent
import ui.screens.master_detail.IMasterDetailComponent
import ui.screens.projects_list.IProjectsListComponent
import ui.screens.task_details.ITaskDetailsComponent
import ui.screens.tasks_list.ITasksListComponent
import ui.screens.teams_list.ITeamsListComponent
import ui.screens.user_details.IUserDetailsComponent

interface IRootComponent {

    val navHostStack: Value<ChildStack<*, NavHost>>
//    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>
//    val toolbarUtilsStack: Value<ChildStack<*, ToolbarUtils>>

    val userLoggingInfo: Flow<UserLoggingInfo>
    val currentDestination: Value<NavItem>

    fun createNewUser(user: User)
    fun navigateTo(navItem: NavItem)

    sealed class NavHost {
//        class UserDetails(val component: IUserDetailsComponent) : NavHost()
//        class TasksList(val component: ITasksListComponent) : NavHost()
        class ProjectMasterDetail(val component: IMasterDetailComponent<Project>) : NavHost()
        class TaskMasterDetail(val component: IMasterDetailComponent<Task>) : NavHost()

        class UserMasterDetail(val component: IMasterDetailComponent<User>) : NavHost()
        class TeamMasterDetail(val component: IMasterDetailComponent<domain.Team>) : NavHost()
//        class TaskDetails(val component: ITaskDetailsComponent) : NavHost()
        class Activity : NavHost()
//        class Projects(val component: IProjectsListComponent) : NavHost()
//        class Team(val component: ITeamsListComponent) : NavHost()
    }

    sealed class Dialog {
        object None : Dialog()
    }

    sealed class ToolbarUtils {
//        class SampleTypesSelector(val component: ISampleTypesSelector) : ToolbarUtils()
    }


    data class UserLoggingInfo(
        val userID: String = "",
        val user: User? = null,
    )
}