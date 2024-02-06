package ui.screens.root_ui

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.Project
import domain.Task
import domain.User
import domain.settings.ISettingDescriptor
import domain.settings.Setting
import kotlinx.coroutines.flow.Flow
import ui.NavItem
import ui.dialogs.entity_picker_dialogs.IBaseEntityPickerDialogComponent
import ui.screens.master_detail.IMasterDetailComponent
import ui.screens.master_detail.settings.SettingsSection

interface IRootComponent {

    val navHostStack: Value<ChildStack<*, NavHost>>
    val dialogSlot: Value<ChildSlot<*, Dialog>>
//    val toolbarUtilsStack: Value<ChildStack<*, ToolbarUtils>>

    val userLoggingInfo: Flow<UserLoggingInfo>
    val currentDestination: Value<NavItem>

    val currentDBPathSetting: Value<Setting>
    val settingDescriptor: ISettingDescriptor
    fun setNewDBPath(dbFile: String)

    fun dismissDialog()
    fun createNewUser(user: User)
    fun navigateTo(navItem: NavItem)

    val navController: INavController

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

        class Settings(val component: IMasterDetailComponent<SettingsSection>) : NavHost()
    }

    sealed class Dialog {
        object None : Dialog()

        class PickerDialog<T>(val component: IBaseEntityPickerDialogComponent<T>) : Dialog()
//        class TeamPickerDialog(val component: IBaseEntityPickerDialogComponent<Team>) : Dialog()
    }

    sealed class ToolbarUtils {
//        class SampleTypesSelector(val component: ISampleTypesSelector) : ToolbarUtils()
    }


    data class UserLoggingInfo(
        val userID: String = "",
        val user: User? = null,
    )
}