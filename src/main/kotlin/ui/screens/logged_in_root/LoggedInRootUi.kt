package ui.screens.logged_in_root

import LocalCurrentUser
import LocalNavController
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.User
import domain.settings.SettingType
import ui.SideNavigationPanel
import ui.dialogs.entity_picker_dialogs.EntityPickerDialogUi
import ui.screens.activity.ActivityUi
import ui.screens.master_detail.MasterDetailsUi
import ui.screens.master_detail.settings.RenderPathSetting
import ui.screens.master_detail.settings.SettingsDetailsUi
import ui.screens.master_detail.settings.SettingsListUi
import ui.screens.project_details.ProjectDetailsUi
import ui.screens.projects_list.ProjectsListUi
import ui.screens.task_details.TaskDetailsUi
import ui.screens.tasks_list.TasksListUi
import ui.screens.team_details.TeamDetailsUi
import ui.screens.teams_list.TeamsListUi
import ui.screens.user_details.UserDetailsUi
import ui.screens.users_list.UsersListUi
import utils.log


@OptIn(ExperimentalDecomposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun LoggedInRootUi(
    component: ILoggedInRootComponent
) {


    val scope = rememberCoroutineScope()
//    val sampleTypes by remember(component) { component.sampleTypes }.subscribeAsState()

    val dialogSlot by remember(component) { component.dialogSlot }.subscribeAsState()



    val navigationItem by remember(component) { component.currentDestination }.subscribeAsState()

    val navController = remember(component) { component.navController }

    CompositionLocalProvider(
//        LocalCurrentUser provides currentlyLoggedUser.user,
        LocalNavController provides navController
    ) {
        //providing current user and navcontroller for all children views
        Row {
            SideNavigationPanel(
                isExpandable = false,
                withLabels = true,
                currentSelection = navigationItem,
                onNavigationItemSelected = {
                    component.navigateTo(it)
                })
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Children(stack = component.navHostStack, animation = stackAnimation(fade())) {
                    when (val child = it.instance) {
                        is ILoggedInRootComponent.NavHost.Activity -> ActivityUi()
                        is ILoggedInRootComponent.NavHost.TaskMasterDetail -> MasterDetailsUi(
                            component = child.component,
                            renderItemsList = { c ->
                                TasksListUi(c)
                            }
                        ) { c ->
                            TaskDetailsUi(c)
                        }

                        is ILoggedInRootComponent.NavHost.ProjectMasterDetail -> MasterDetailsUi(
                            component = child.component,
                            renderItemsList = { c ->
                                ProjectsListUi(c)
                            }
                        ) { c ->
                            ProjectDetailsUi(c)
                        }

                        is ILoggedInRootComponent.NavHost.TeamMasterDetail -> MasterDetailsUi(
                            component = child.component,
                            renderItemsList = { c ->
                                TeamsListUi(c)
                            }
                        ) { c ->
                            TeamDetailsUi(c)
                        }

                        is ILoggedInRootComponent.NavHost.UserMasterDetail -> MasterDetailsUi(
                            component = child.component,
                            renderItemsList = { c ->
                                UsersListUi(c)
                            }
                        ) { c ->
                            UserDetailsUi(c)
                        }

                        is ILoggedInRootComponent.NavHost.Settings -> MasterDetailsUi(
                            component = child.component,
                            renderItemsList = { c ->
                                SettingsListUi(c)
                            },
                            renderItemDetails = { c ->
                                SettingsDetailsUi(c)
                            }
                        )
                    }
                }
            }
        }
    }




    //dialogs:

    dialogSlot.child?.instance?.also { dialogComponent ->
        when (dialogComponent) {

            ILoggedInRootComponent.Dialog.None -> {
                //show nothing
            }

            is ILoggedInRootComponent.Dialog.PickerDialog<*> -> {
                EntityPickerDialogUi(
                    component = dialogComponent.component,
                    onDismiss = {
                        component.dismissDialog()
                    }
                )
            }
        }
    }

}

