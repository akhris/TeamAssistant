package ui.screens.master_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import domain.IEntity
import ui.UiSettings
import ui.screens.master_detail.details.DetailsUi
import ui.screens.master_detail.master.MasterUi

@Composable
fun <T : IEntity> MasterDetailUi(
    component: IMasterDetailComponent<T>,
    renderListItem: @Composable (item: T) -> Unit,
    renderItemDetails: @Composable (item: T) -> Unit
) {

    Row {
        Box(modifier = Modifier
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
        Box(modifier = Modifier
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
}