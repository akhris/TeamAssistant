package ui.screens.root_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import tests.testTask1
import tests.testTask2
import tests.testTeam1
import tests.testUser1
import ui.SideNavigationPanel
import ui.screens.activity.ActivityUi
import ui.screens.projects.ProjectsUi
import ui.screens.tasks.TasksUi
import ui.screens.team.TeamUi
import ui.screens.user_details.UserDetailsUi
import utils.log


@OptIn(ExperimentalDecomposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun FrameWindowScope.RootUi(
    component: IRootComponent,
    isDarkTheme: Boolean,
    onThemeChanged: (isDark: Boolean) -> Unit,
    windowState: WindowState
) {

    val scaffoldState = rememberScaffoldState()
    val navigationItem by remember(component) { component.currentDestination }.subscribeAsState()
//    val sampleTypes by remember(component) { component.sampleTypes }.subscribeAsState()

    val currentlyLoggedUser by component.userLoggingInfo.collectAsState(IRootComponent.UserLoggingInfo())

    remember(currentlyLoggedUser) {  log(currentlyLoggedUser, prefix = "logged user: ") }

    when(currentlyLoggedUser.user){
        null->{
            //show new user screen
        }
        else->{
            //show main screen
        }
    }

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
                }
            }
        },
        content = {


            Row {
                SideNavigationPanel(
                    isExpandable = false,
                    withLabels = true,
                    currentSelection = navigationItem,
                    onNavigationItemSelected = { component.navigateTo(it) })
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Children(stack = component.navHostStack, animation = stackAnimation(fade())) {
                        when (val child = it.instance) {
                            is IRootComponent.NavHost.Activity -> ActivityUi()
                            is IRootComponent.NavHost.Projects -> ProjectsUi()
                            is IRootComponent.NavHost.Tasks -> TasksUi(listOf(testTask1, testTask2))
                            is IRootComponent.NavHost.Team -> TeamUi(testTeam1)
                            is IRootComponent.NavHost.UserDetails -> UserDetailsUi(testUser1)
                        }
                    }
                }
            }

        }
    )

//    Children(stack = component.dialogStack, animation = stackAnimation(slide())) {
//        when (val child = it.instance) {
//            IRootComponent.Dialog.None -> {}
//            is IRootComponent.Dialog.AddSampleTypeDialog -> AddSampleTypeDialogUi(child.component, onDismiss = {
//                component.dismissDialog()
//            })
//        }
//    }


}