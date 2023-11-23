package ui.screens.teams

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Team
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.launch
import tests.testTeam1
import ui.FABState
import ui.IFABController
import utils.log


@Composable
fun TeamsUi(teamsListComponent: ITeamsListComponent, fabController: IFABController) {
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Команды",
            style = MaterialTheme.typography.h5
        )
    }

    LaunchedEffect(fabController) {
        log("showing TeamsUi with component: $teamsListComponent and fabController: $fabController")
        fabController.setFABState(FABState.VISIBLE)

    }

    scope.launch {
        fabController.clicks.collect {
            log("fab clicked on teamsUI")
        }
    }
}

@Preview
@Composable
fun userDetailsPreview() {
//    TeamsUi(testTeam1)
}