package ui.screens.teams

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.FABState
import ui.IFABController
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.TextInputDialogUi
import utils.log


@Composable
fun TeamsUi(teamsListComponent: ITeamsListComponent, fabController: IFABController) {

    val dialogSlot by remember(teamsListComponent) { teamsListComponent.dialogSlot }.subscribeAsState()

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


    dialogSlot.child?.instance?.also { comp ->
        when (comp) {
            is IDialogComponent.ITextInputDialogComponent -> {
                TextInputDialogUi(comp)
            }
        }

    }





    LaunchedEffect(fabController) {
        fabController.setFABState(
            FABState.VISIBLE(
                iconPath = "vector/add_black_24dp.svg",
                text = "добавить команду",
                description = "добавить новую команду"
            )
        )
        fabController
            .clicks
            .collect {
                teamsListComponent.createNewTeam()
                log("fab clicked on teamsUI")
            }
    }
}

@Preview
@Composable
fun userDetailsPreview() {
//    TeamsUi(testTeam1)
}