package ui.screens.root_ui

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
import kotlinx.coroutines.launch
import ui.FABController
import ui.FABState
import ui.SideNavigationPanel
import ui.dialogs.entity_picker_dialogs.EntityPickerDialogUi
import ui.screens.activity.ActivityUi
import ui.screens.master_detail.MasterDetailsUi
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
fun FrameWindowScope.RootUi(
    component: IRootComponent,
    isDarkTheme: Boolean,
    onThemeChanged: (isDark: Boolean) -> Unit,
    windowState: WindowState,
    onCloseRequest: () -> Unit,
) {


    val scope = rememberCoroutineScope()
//    val sampleTypes by remember(component) { component.sampleTypes }.subscribeAsState()
    val currentlyLoggedUser by remember(component) { component.userLoggingInfo }.collectAsState(IRootComponent.UserLoggingInfo())

    val dialogSlot by remember(component) { component.dialogSlot }.subscribeAsState()


    log("currentlyLoggedUser: $currentlyLoggedUser")

    val scaffoldState = rememberScaffoldState()

    val navigationItem by remember(component) { component.currentDestination }.subscribeAsState()

    val navController = remember(component) { component.navController }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            WindowDraggableArea {
                TopAppBar {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "TeamAssistant")
//                Children(stack = component.toolbarUtilsStack) {
//                    when (val child = it.instance) {
//                        is IRootComponent.ToolbarUtils.SampleTypesSelector -> SampleTypesSelectorUi(
//                            component = child.component,
//                            onSampleTypeSelected = {
//                                selectedSampleType = it
//                            }, onAddNewSampleTypeClick = {
//                                component.showAddSampleTypeDialog()
//                            })
//                    }
//                }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp).clickable {
                                onThemeChanged(!isDarkTheme)
                            },
                        painter = when (isDarkTheme) {
                            true -> painterResource("vector/light_mode_black_24dp.svg")
                            false -> painterResource("vector/dark_mode_black_24dp.svg")
                        }, contentDescription = "light/dark theme switcher"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp).clickable {
                                windowState.isMinimized = !windowState.isMinimized
                            },
                        painter = painterResource("vector/minimize_black_24dp.svg"), contentDescription = "minimize"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp).clickable {
                                windowState.placement = if (windowState.placement == WindowPlacement.Maximized)
                                    WindowPlacement.Floating else WindowPlacement.Maximized
                            },
                        painter = when (windowState.placement) {
                            WindowPlacement.Floating -> painterResource("vector/fullscreen_black_24dp.svg")
                            WindowPlacement.Maximized -> painterResource("vector/fullscreen_exit_black_24dp.svg")
                            WindowPlacement.Fullscreen -> throw UnsupportedOperationException("fullscreen mode is not yet supported")
                        }, contentDescription = "maximize"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp).clickable {
                                onCloseRequest()
                            },
                        painter = painterResource("vector/close_black_24dp.svg"), contentDescription = "close app"
                    )
                }
            }
        },
        content = {
            if (currentlyLoggedUser.user == null && currentlyLoggedUser.userID.isNotEmpty()) {
                //show new user ui
                var tempUser by remember(currentlyLoggedUser.userID) { mutableStateOf(User(id = currentlyLoggedUser.userID)) }
                Box(modifier = Modifier.padding(it)) {
                    NewUserUi(
                        user = tempUser,
                        onUserChange = { user ->
                            log(user, "onUserChange: ")
                            tempUser = user
                        },
                        onUserCreate = {
                            //save user to DB
                            component.createNewUser(tempUser)
                        }
                    )
                }
            } else if (currentlyLoggedUser.user != null) {
                //show main stuff
                CompositionLocalProvider(
                    LocalCurrentUser provides currentlyLoggedUser.user,
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
                                    is IRootComponent.NavHost.Activity -> ActivityUi()
                                    is IRootComponent.NavHost.TaskMasterDetail -> MasterDetailsUi(
                                        component = child.component,
                                        renderItemsList = { c ->
                                            TasksListUi(c)
                                        }
                                    ) { c ->
                                        TaskDetailsUi(c)
                                    }

                                    is IRootComponent.NavHost.ProjectMasterDetail -> MasterDetailsUi(
                                        component = child.component,
                                        renderItemsList = { c ->
                                            ProjectsListUi(c)
                                        }
                                    ) { c ->
                                        ProjectDetailsUi(c)
                                    }

                                    is IRootComponent.NavHost.TeamMasterDetail -> MasterDetailsUi(
                                        component = child.component,
                                        renderItemsList = { c ->
                                            TeamsListUi(c)
                                        }
                                    ) { c ->
                                        TeamDetailsUi(c)
                                    }

                                    is IRootComponent.NavHost.UserMasterDetail -> MasterDetailsUi(
                                        component = child.component,
                                        renderItemsList = { c ->
                                            UsersListUi(c)
                                        }
                                    ) { c ->
                                        UserDetailsUi(c)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                //show nothing
            }

        }
    )


    //dialogs:

    dialogSlot.child?.instance?.also { dialogComponent ->
        when (dialogComponent) {

            IRootComponent.Dialog.None -> {
                //show nothing
            }
            is IRootComponent.Dialog.PickerDialog<*> -> {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NewUserUi(
    user: User,
    onUserChange: (User) -> Unit,
    onUserCreate: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "новый пользователь", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = user.name, onValueChange = {
            onUserChange(user.copy(name = it))
        }, placeholder = {
            Text("имя")
        })

        TextField(value = user.middleName, onValueChange = {
            onUserChange(user.copy(middleName = it))
        }, placeholder = {
            Text("отчество")
        })
        TextField(value = user.surname, onValueChange = {
            onUserChange(user.copy(surname = it))
        }, placeholder = {
            Text("фамилия")
        })
        Button(onClick = onUserCreate) {
            Text("Создать")
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "id: ${user.id}", style = MaterialTheme.typography.caption)
    }

}