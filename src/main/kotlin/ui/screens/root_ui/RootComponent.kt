package ui.screens.root_ui

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.instance
import ui.NavItem
import ui.dialogs.DialogProperties
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent
import ui.screens.master_detail.projects.ProjectsMasterDetailsComponent
import ui.screens.master_detail.tasks.TasksMasterDetailsComponent
import ui.screens.master_detail.teams.TeamsMasterDetailsComponent
import ui.screens.master_detail.users.UsersMasterDetailsComponent
import ui.screens.tasks_list.TasksListComponent
import ui.screens.user_details.UserDetailsComponent
import utils.UserUtils

class RootComponent(
    private val di: DI,
    componentContext: ComponentContext,
) : IRootComponent, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val userID = UserUtils.getUserID()

    private val usersRepo: IRepositoryObservable<User> by di.instance()
    private val tasksRepo: IRepositoryObservable<Task> by di.instance()
    private val projectsRepo: IRepositoryObservable<Project> by di.instance()
    private val teamsRepo: IRepositoryObservable<Team> by di.instance()

//    private val dialogNav = SlotNavigation<DialogConfig>()
    private val navHostNav = StackNavigation<NavHostConfig>()
//    private val toolbarUtilsNav = StackNavigation<ToolbarUtilsConfig>()

//    override val dialogSlot: Value<ChildSlot<*, IDialogComponent>> =
//        childSlot(
//            source = dialogNav,
//            // persistent = false, // Disable navigation state saving, if needed
//            handleBackButton = true, // Close the dialog on back button press
//        ) { config, childComponentContext ->
//            when (config) {
//                DialogConfig.None -> IDialogComponent.NONE
//                is DialogConfig.CreateNewEntityDialog -> getEntityDialog(childComponentContext, config.entityClass)
//                    ?: IDialogComponent.NONE
//            }
//        }

//    private fun getEntityDialog(
//        componentContext: ComponentContext,
//        entityClass: Class<out IEntity>,
//    ): IDialogComponent? {
//
//        val props = when (entityClass) {
//            Project::class.java -> DialogProperties(
//                title = "добавить проект",
//                hint = "имя проекта",
//                OKButtonText = "добавить"
//            )
//
//            Task::class.java -> DialogProperties(
//                title = "добавить задачу",
//                hint = "имя задачи",
//                OKButtonText = "добавить"
//            )
//
//            Team::class.java -> DialogProperties(
//                title = "добавить команду",
//                hint = "имя команды",
//                OKButtonText = "добавить"
//            )
//
//            User::class.java -> DialogProperties(
//                title = "добавить пользователя",
//                hint = "имя пользователя",
//                OKButtonText = "добавить"
//            )
//
//            else -> null
//        }
//
//        return props?.let {
//            DialogTextInputComponent(
//                componentContext = componentContext,
//                properties = it,
//                onDismissed = dialogNav::dismiss
//            )
//        }
//    }

    private val _currentDestination = MutableValue<NavItem>(NavItem.homeItem)
    override val currentDestination: Value<NavItem> = _currentDestination

    //    override val userLoggingInfo: Value<IRootComponent.UserLoggingInfo> = _userLoggingInfo

    override val userLoggingInfo: Flow<IRootComponent.UserLoggingInfo> = usersRepo
        .getByID(userID)
        .map { result ->
            when (result) {
                is RepoResult.InitialItem -> result.item
                is RepoResult.ItemInserted -> result.item
                is RepoResult.ItemRemoved -> null
                is RepoResult.ItemUpdated -> result.item
                is RepoResult.PendindObject -> null
            }
        }
        .map {
            IRootComponent.UserLoggingInfo(userID = userID, user = it)
        }


    override val navHostStack: Value<ChildStack<*, IRootComponent.NavHost>> = childStack(
        source = navHostNav,
        initialConfiguration = NavItem.homeItem.toNavHostConfig(),
//            handleBackButton = true,
        childFactory = ::createChild,
        key = "navhost stack"
    )


    override fun createNewUser(user: User) {
        scope.launch {
            //make insert user usecase
            usersRepo.insert(user)
        }
    }


//    override fun createNewEntityDialog() {
//        val newEntityClass = when (navHostStack.active.instance) {
//            is IRootComponent.NavHost.Activity -> null
//            is IRootComponent.NavHost.ProjectMasterDetail -> Project::class.java
//            is IRootComponent.NavHost.TaskMasterDetail -> Task::class.java
//            is IRootComponent.NavHost.TeamMasterDetail -> Team::class.java
//            is IRootComponent.NavHost.UserMasterDetail -> User::class.java
//        }
//        newEntityClass?.let {
//            dialogNav.activate(DialogConfig.CreateNewEntityDialog(it))
//        }
//    }


    private fun createChild(config: NavHostConfig, componentContext: ComponentContext): IRootComponent.NavHost {
        return when (config) {
            NavHostConfig.Activity -> IRootComponent.NavHost.Activity()
//            NavHostConfig.Projects -> IRootComponent.NavHost.Projects(ProjectsListComponent(di, componentContext, {}))

            NavHostConfig.TasksList -> IRootComponent.NavHost.TaskMasterDetail(
                TasksMasterDetailsComponent(di = di, componentContext = componentContext)
            )

//            NavHostConfig.Team -> IRootComponent.NavHost.Team(TeamsListComponent(di, componentContext))
            NavHostConfig.UsersList -> IRootComponent.NavHost.UserMasterDetail(
                UsersMasterDetailsComponent(di = di, componentContext = componentContext)
            )

//            is NavHostConfig.TaskDetails -> IRootComponent.NavHost.TaskDetails(
//                TaskDetailsComponent(taskID = config.task.id, di = di, componentContext = componentContext)
//            )

            NavHostConfig.ProjectsList -> IRootComponent.NavHost.ProjectMasterDetail(
                ProjectsMasterDetailsComponent(di = di, componentContext = componentContext)
            )

            NavHostConfig.TeamsList -> IRootComponent.NavHost.TeamMasterDetail(
                TeamsMasterDetailsComponent(di = di, componentContext = componentContext)
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
        }
    }

    @Parcelize
    private sealed class DialogConfig : Parcelable {
//
//        @Parcelize
//        object AddSampleType : DialogConfig()

        @Parcelize
        data class CreateNewEntityDialog(val entityClass: Class<out IEntity>) : DialogConfig()

        @Parcelize
        object None : DialogConfig()

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

//        @Parcelize
//        object Projects : NavHostConfig()

//        @Parcelize
//        object Team : NavHostConfig()

    }

    @Parcelize
    private sealed class ToolbarUtilsConfig : Parcelable {
//        object SampleTypesSelector : ToolbarUtilsConfig()
    }


    init {
        componentContext
            .lifecycle
            .subscribe(onDestroy = {
                scope.coroutineContext.cancelChildren()
            })

    }

}