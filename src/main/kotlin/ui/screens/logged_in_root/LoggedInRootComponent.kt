package ui.screens.logged_in_root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.*
import kotlinx.coroutines.*
import org.kodein.di.DI
import settings.Settings
import ui.NavItem
import ui.dialogs.entity_picker_dialogs.ProjectPickerComponent
import ui.dialogs.entity_picker_dialogs.TeamPickerComponent
import ui.dialogs.entity_picker_dialogs.UserPickerComponent
import ui.screens.BaseComponent
import ui.screens.master_detail.projects.ProjectsMasterDetailsComponent
import ui.screens.master_detail.settings.SettingsMasterDetailsComponent
import ui.screens.master_detail.tasks.TasksMasterDetailsComponent
import ui.screens.master_detail.teams.TeamsMasterDetailsComponent
import ui.screens.master_detail.users.UsersMasterDetailsComponent

class LoggedInRootComponent(
    private val di: DI,
    componentContext: ComponentContext,
    private val dbPath: String,     //fixme maybe use Realm instance? This allows to close & open realm files
    private val loggedUser: User,
) : ILoggedInRootComponent, BaseComponent(componentContext) {


    private val dialogNav = SlotNavigation<DialogConfig>()
    private val navHostNav = StackNavigation<NavHostConfig>()
//    private val toolbarUtilsNav = StackNavigation<ToolbarUtilsConfig>()

    override val dialogSlot: Value<ChildSlot<*, ILoggedInRootComponent.Dialog>> =
        childSlot(
            source = dialogNav,
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            when (config) {
                DialogConfig.None -> ILoggedInRootComponent.Dialog.None
                is DialogConfig.UserPickerDialog -> ILoggedInRootComponent.Dialog.PickerDialog(
                    component = UserPickerComponent(
                        isMultipleSelection = config.isMultipleSelection,
                        initialSelection = config.initialSelection,
                        onUsersPicked = config.onUsersPicked,
                        hiddenUsers = config.hiddenUsers,
                        di = di,
                        componentContext = childComponentContext
                    )
                )

                is DialogConfig.TeamPickerDialog -> ILoggedInRootComponent.Dialog.PickerDialog(
                    component = TeamPickerComponent(
                        isMultipleSelection = config.isMultipleSelection,
                        initialSelection = config.initialSelection,
                        onTeamsPicked = config.onTeamsPicked,
                        di,
                        childComponentContext
                    )
                )

                is DialogConfig.ProjectPickerDialog -> ILoggedInRootComponent.Dialog.PickerDialog(
                    component = ProjectPickerComponent(
                        isMultipleSelection = config.isMultipleSelection,
                        initialSelection = config.initialSelection,
                        onProjectsPicked = config.onProjectsPicked,
                        di,
                        childComponentContext
                    )
                )
            }
        }

    override fun dismissDialog() {
        dialogNav.dismiss()
    }


    private val _currentDestination = MutableValue<NavItem>(NavItem.homeItem)
    override val currentDestination: Value<NavItem> = _currentDestination

    //    override val userLoggingInfo: Value<IRootComponent.UserLoggingInfo> = _userLoggingInfo


    override val navHostStack: Value<ChildStack<*, ILoggedInRootComponent.NavHost>> = childStack(
        source = navHostNav,
        initialConfiguration = NavItem.homeItem.toNavHostConfig(),
//            handleBackButton = true,
        childFactory = ::createChild,
        key = "navhost stack"
    )


    override val navController: INavController = object : INavController {
        override fun showUsersPickerDialog(
            isMultipleSelection: Boolean,
            initialSelection: List<User>,
            onUsersPicked: (List<User>) -> Unit,
            hiddenUsers: List<User>,
        ) {
            dialogNav.activate(
                DialogConfig.UserPickerDialog(
                    isMultipleSelection = isMultipleSelection,
                    initialSelection = initialSelection,
                    onUsersPicked = onUsersPicked,
                    hiddenUsers = hiddenUsers
                )
            )
        }

        override fun showTeamsPickerDialog(
            isMultipleSelection: Boolean,
            initialSelection: List<Team>,
            onTeamsPicked: (List<Team>) -> Unit,
        ) {
            dialogNav.activate(
                DialogConfig.TeamPickerDialog(
                    isMultipleSelection = isMultipleSelection,
                    initialSelection = initialSelection,
                    onTeamsPicked = onTeamsPicked
                )
            )
        }

        override fun showProjectsPickerDialog(
            isMultipleSelection: Boolean,
            initialSelection: List<Project>,
            onProjectsPicked: (List<Project>) -> Unit,
        ) {
            dialogNav.activate(
                DialogConfig.ProjectPickerDialog(
                    isMultipleSelection = isMultipleSelection,
                    initialSelection = initialSelection,
                    onProjectsPicked = onProjectsPicked
                )
            )
        }
    }


    private fun createChild(config: NavHostConfig, componentContext: ComponentContext): ILoggedInRootComponent.NavHost {
        return when (config) {
            NavHostConfig.Activity -> ILoggedInRootComponent.NavHost.Activity()
//            NavHostConfig.Projects -> IRootComponent.NavHost.Projects(ProjectsListComponent(di, componentContext, {}))

            NavHostConfig.TasksList -> ILoggedInRootComponent.NavHost.TaskMasterDetail(
                TasksMasterDetailsComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = dbPath,
                    currentUser = loggedUser
                )
            )

//            NavHostConfig.Team -> IRootComponent.NavHost.Team(TeamsListComponent(di, componentContext))
            NavHostConfig.UsersList -> ILoggedInRootComponent.NavHost.UserMasterDetail(
                UsersMasterDetailsComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = dbPath,
                    currentUser = loggedUser
                )
            )

//            is NavHostConfig.TaskDetails -> IRootComponent.NavHost.TaskDetails(
//                TaskDetailsComponent(taskID = config.task.id, di = di, componentContext = componentContext)
//            )

            NavHostConfig.ProjectsList -> ILoggedInRootComponent.NavHost.ProjectMasterDetail(
                ProjectsMasterDetailsComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = dbPath,
                    currentUser = loggedUser
                )
            )

            NavHostConfig.TeamsList -> ILoggedInRootComponent.NavHost.TeamMasterDetail(
                TeamsMasterDetailsComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = dbPath,
                    currentUser = loggedUser
                )
            )

            NavHostConfig.Settings -> ILoggedInRootComponent.NavHost.Settings(
                SettingsMasterDetailsComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = dbPath,
                    currentUser = loggedUser
                )
            )
        }
    }


//    private fun createChild(
//        config: ToolbarUtilsConfig,
//        componentContext: ComponentContext
//    ): IRootComponent.ToolbarUtils {
////        return when (config) {
////            ToolbarUtilsConfig.SampleTypesSelector -> IRootComponent.ToolbarUtils.SampleTypesSelector(
//////                SampleTypesSelectorComponent(di = di, componentContext = componentContext)
////            )
////        }
//    }


    override fun navigateTo(navItem: NavItem) {
        val newConf = navItem.toNavHostConfig()
        if (newConf != null && newConf != navHostStack.value.active.configuration) {
            navHostNav.replaceCurrent(newConf)
            _currentDestination.value = navItem
        }
    }

    private fun NavItem.toNavHostConfig(): NavHostConfig {
        return when (this) {
            NavItem.Activity -> NavHostConfig.Activity
            NavItem.Projects -> NavHostConfig.ProjectsList
            NavItem.Tasks -> NavHostConfig.TasksList
            NavItem.Team -> NavHostConfig.TeamsList
            NavItem.UserDetails -> NavHostConfig.UsersList
            NavItem.Settings -> NavHostConfig.Settings
        }
    }

    @Parcelize
    private sealed class DialogConfig : Parcelable {
        @Parcelize
        object None : DialogConfig()

        @Parcelize
        data class UserPickerDialog(
            val isMultipleSelection: Boolean = false,
            val initialSelection: List<User> = listOf(),
            val onUsersPicked: (List<User>) -> Unit,
            val hiddenUsers: List<User> = listOf(),
        ) : DialogConfig()

        @Parcelize
        data class TeamPickerDialog(
            val isMultipleSelection: Boolean = false,
            val initialSelection: List<Team> = listOf(),
            val onTeamsPicked: (List<Team>) -> Unit,
        ) : DialogConfig()

        @Parcelize
        data class ProjectPickerDialog(
            val isMultipleSelection: Boolean = false,
            val initialSelection: List<Project> = listOf(),
            val onProjectsPicked: (List<Project>) -> Unit,
        ) : DialogConfig()


    }

    @Parcelize
    private sealed class NavHostConfig : Parcelable {
        @Parcelize
        object UsersList : NavHostConfig()

        @Parcelize
        object TasksList : NavHostConfig()

//        @Parcelize
//        class TaskDetails(val task: Task) : NavHostConfig()

        @Parcelize
        object TeamsList : NavHostConfig()

        @Parcelize
        object ProjectsList : NavHostConfig()

        @Parcelize
        object Activity : NavHostConfig()

        @Parcelize
        object Settings : NavHostConfig()


//        @Parcelize
//        object Projects : NavHostConfig()

//        @Parcelize
//        object Team : NavHostConfig()

    }

    @Parcelize
    private sealed class ToolbarUtilsConfig : Parcelable {
//        object SampleTypesSelector : ToolbarUtilsConfig()
    }


}