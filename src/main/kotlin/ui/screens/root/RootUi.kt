package ui.screens.root

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.db_selector.DBSelectorUi
import ui.screens.logged_in_root.LoggedInRootUi
import ui.screens.user_create.UserCreateUi
import kotlin.io.path.Path

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FrameWindowScope.RootUi(
    component: IRootComponent,
    isDarkTheme: Boolean,
    onThemeChanged: (isDark: Boolean) -> Unit,
    windowState: WindowState,
    onCloseRequest: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val currentDBPath by remember(component) { component.currentDBPath }.subscribeAsState()

    Scaffold(
        scaffoldState = scaffoldState,
//        snackbarHost = {
//            SnackbarHost(hostState = snackbarHostState)
//        },
        topBar = {
            WindowDraggableArea {
                TopAppBar {
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "TeamAssistant")
                        if (currentDBPath.isNotEmpty()) {
                            Text(modifier = Modifier.padding(end = 16.dp), text = " : ")
//                            Spacer(modifier = Modifier.width(16.dp))
                            TooltipArea(tooltip = {
                                Surface {
                                    Text(text = currentDBPath)
                                }
                            }, content = {
                                Text(
                                    modifier = Modifier.clickable {
                                        component.loadDatabase()
                                    },
                                    text = Path(currentDBPath).fileName.toString(),
                                    textDecoration = TextDecoration.Underline
                                )
                            })

                        }
                    }
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
            Children(stack = component.rootStack, animation = stackAnimation(fade())) {
                when (val child = it.instance) {
                    is IRootComponent.Screen.DBSelector -> {
                        DBSelectorUi(component = child.component)
                    }

                    is IRootComponent.Screen.UserCreate -> {
                        UserCreateUi(component = child.component)
                    }

                    is IRootComponent.Screen.LoggedIn -> {
                        LoggedInRootUi(component = child.component)
                    }

                    IRootComponent.Screen.NONE -> {
                        //show nothing
                    }
                }
            }


        }
    )
}