package ui.screens.master_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.IEntity
import ui.FABState
import ui.IFABController
import ui.UiSettings
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.TextInputDialogUi
import ui.screens.master_detail.details.DetailsUi
import ui.screens.master_detail.master.MasterUi

@Composable
fun <T : IEntity> MasterDetailUi(
    component: IMasterDetailComponent<T>,
    renderListItem: @Composable (item: T) -> Unit,
    renderItemDetails: @Composable (item: T) -> Unit,
    fabController: IFABController,
    onCreateNewItem: (name: String) -> T,
) {

    val fabState by remember(component) { component.fabState }.subscribeAsState()

    val dialogSlot by remember(component) { component.dialogSlot }.subscribeAsState()


    Row {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1 / 3f)
                .padding(UiSettings.Screen.screenPadding)
//            .border(2.dp, color = Color.Green)
        ) {
            //Entities List
            Children(stack = component.masterStack, animation = stackAnimation(fade())) {
                when (val child = it.instance) {
                    is IMasterDetailComponent.Master.ItemsList -> {
                        MasterUi(
                            component = child.component,
                            renderListItem = renderListItem
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2 / 3f)
                .padding(UiSettings.Screen.screenPadding)
//            .border(2.dp, color = Color.Red)
        ) {
            //Details screen
            Children(stack = component.detailsStack, animation = stackAnimation(fade())) {
                when (val child = it.instance) {
                    is IMasterDetailComponent.Details.ItemDetails -> {
                        DetailsUi(child.component, renderItemDetails)
                    }

                    else -> {
                        //show empty screen
                    }
                }
            }
        }

    }


    dialogSlot.child?.instance?.also { comp ->
        when (comp) {
            is IDialogComponent.ITextInputDialogComponent -> {
                TextInputDialogUi(
                    component = comp,
                    onOkClicked = { name ->
                        component.createNewItem(
                            onCreateNewItem(name)
                        )
                    }
                )
            }
        }

    }

    LaunchedEffect(fabController, fabState) {
        fabController.setFABState(fabState)
        fabController
            .clicks
            .collect {
                component.onFABClicked()
            }
    }
}