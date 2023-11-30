package ui.screens.root_ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
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
import ui.screens.projects_list.ProjectsListComponent
import ui.screens.task_details.TaskDetailsComponent
import ui.screens.tasks_list.TasksListComponent
import ui.screens.teams_list.TeamsListComponent
import ui.screens.user_details.UserDetailsComponent
import utils.UserUtils

class RootComponent(
    private val di: DI,
    componentContext: ComponentContext
) : IRootComponent, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val userID = UserUtils.getUserID()
    private val repo: IRepositoryObservable<User> by di.instance()

    private val dialogNav = StackNavigation<DialogConfig>()
    private val navHostNav = StackNavigation<NavHostConfig>()
//    private val toolbarUtilsNav = StackNavigation<ToolbarUtilsConfig>()

    private val _currentDestination = MutableValue<NavItem>(NavItem.homeItem)
    override val currentDestination: Value<NavItem> = _currentDestination

    //    override val userLoggingInfo: Value<IRootComponent.UserLoggingInfo> = _userLoggingInfo

    override val userLoggingInfo: Flow<IRootComponent.UserLoggingInfo> = repo
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

//        realm
//            .query<RealmUser>("_id == $0", userID)
//            .first()
//            .asFlow()
//            .map {
//                val user = when (it) {
//                    is DeletedObject -> it.obj
//                    is InitialObject -> it.obj
//                    is UpdatedObject -> it.obj
//                    is PendingObject -> it.obj
//                }?.toUser()
//                IRootComponent.UserLoggingInfo(userID = userID, user = user)
//            }


    private val _navHostStack =
        childStack(
            source = navHostNav,
            initialConfiguration = NavItem.homeItem.toNavHostConfig(),
//            handleBackButton = true,
            childFactory = ::createChild,
            key = "navhost stack"
        )

    private val _dialogStack =
        childStack(
            source = dialogNav,
            initialConfiguration = DialogConfig.None,
//            handleBackButton = true,
            childFactory = ::createChild,
            key = "dialog stack"
        )

//    private val _toolbarUtilsStack =
//        childStack(
//            source = toolbarUtilsNav,
//            initialConfiguration = ToolbarUtilsConfig.SampleTypesSelector,
//            childFactory = ::createChild,
//            key = "toolbar utils stack"
//        )

    override val navHostStack: Value<ChildStack<*, IRootComponent.NavHost>> = _navHostStack
    override val dialogStack: Value<ChildStack<*, IRootComponent.Dialog>> = _dialogStack
//    override val toolbarUtilsStack: Value<ChildStack<*, IRootComponent.ToolbarUtils>> = _toolbarUtilsStack

//    override fun showAddSampleTypeDialog() {
//        dialogNav.replaceCurrent(DialogConfig.AddSampleType)
//    }

    override fun createNewUser(user: User) {
        scope.launch {
            //make insert user usecase
            repo.insert(user)
        }
    }

    override fun dismissDialog() {
        dialogNav.replaceCurrent(DialogConfig.None)
    }


    private fun createChild(config: DialogConfig, componentContext: ComponentContext): IRootComponent.Dialog {
        return when (config) {
            DialogConfig.None -> IRootComponent.Dialog.None
        }
    }

    private fun createChild(config: NavHostConfig, componentContext: ComponentContext): IRootComponent.NavHost {
        return when (config) {
            NavHostConfig.Activity -> IRootComponent.NavHost.Activity()
            NavHostConfig.Projects -> IRootComponent.NavHost.Projects(ProjectsListComponent(di, componentContext))
            NavHostConfig.TasksList -> IRootComponent.NavHost.TasksList(
                TasksListComponent(
                    di,
                    componentContext,
                    onTaskSelected = {
                        //navigate to task details:
                        navHostNav.replaceCurrent(NavHostConfig.TaskDetails(it))
                    })
            )

            NavHostConfig.Team -> IRootComponent.NavHost.Team(TeamsListComponent(di, componentContext))
            NavHostConfig.UserDetails -> IRootComponent.NavHost.UserDetails(
                UserDetailsComponent(
                    userID = userID,
                    di,
                    componentContext
                )
            )

            is NavHostConfig.TaskDetails -> IRootComponent.NavHost.TaskDetails(
                TaskDetailsComponent(taskID = config.task.id, di, componentContext)
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
        if (newConf != null && newConf != _navHostStack.value.active.configuration) {
            navHostNav.replaceCurrent(newConf)
            _currentDestination.value = navItem
        }
    }

    private fun NavItem.toNavHostConfig(): NavHostConfig {
        return when (this) {
            NavItem.Activity -> NavHostConfig.Activity
            NavItem.Projects -> NavHostConfig.Projects
            NavItem.Tasks -> NavHostConfig.TasksList
            NavItem.Team -> NavHostConfig.Team
            NavItem.UserDetails -> NavHostConfig.UserDetails
        }
    }

    @Parcelize
    private sealed class DialogConfig : Parcelable {
//
//        @Parcelize
//        object AddSampleType : DialogConfig()

        @Parcelize
        object None : DialogConfig()

    }

    @Parcelize
    private sealed class NavHostConfig : Parcelable {
        @Parcelize
        object UserDetails : NavHostConfig()

        @Parcelize
        object TasksList : NavHostConfig()

        @Parcelize
        class TaskDetails(val task: Task) : NavHostConfig()

        @Parcelize
        object Activity : NavHostConfig()

        @Parcelize
        object Projects : NavHostConfig()

        @Parcelize
        object Team : NavHostConfig()

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