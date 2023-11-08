package ui.screens.root_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import tests.testTeam1
import tests.testUser1
import ui.SideNavigationPanel
import ui.screens.team.TeamUi
import ui.screens.user_details.UserDetailsUi


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(component: IRootComponent, isDarkTheme: Boolean, onThemeChanged: (isDark: Boolean) -> Unit) {

    val scaffoldState = rememberScaffoldState()

    val navigationItem by remember(component) { component.currentDestination }.subscribeAsState()
//    val sampleTypes by remember(component) { component.sampleTypes }.subscribeAsState()


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                Spacer(modifier = Modifier.weight(1f))
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
                            is IRootComponent.NavHost.Activity -> TODO()
                            is IRootComponent.NavHost.Projects -> TODO()
                            is IRootComponent.NavHost.Tasks -> TODO()
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