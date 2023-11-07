package ui.screens.main_screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.SideNavigationPanel
import utils.log

@Composable
fun MainScreenUi() {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Row {
                SideNavigationPanel {
                    log("going to navigate to ${it.title}")
                }
                Box(modifier = Modifier.weight(1f)) {

                }
            }
        }
    )
}


@Preview
@Composable
fun MainScreenUiPreview() {
    MainScreenUi()
}