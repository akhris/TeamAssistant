package ui.screens.master_detail

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation

@Composable
fun <T> MasterDetailsUi(
    component: IMasterDetailComponent<T>,
    renderItemsList: @Composable (IMasterComponent<T>) -> Unit,
    renderItemDetails: @Composable (IDetailsComponent<T>) -> Unit
) {
    BaseMasterDetailsUi(
        renderItemDetails = {
            Children(stack = component.detailsStack, animation = stackAnimation(fade())) {
                when (val child = it.instance) {
                    is IMasterDetailComponent.Details.ItemDetails -> {
                        renderItemDetails(child.component)
                    }

                    is IMasterDetailComponent.Details.None -> {
                        //show nothing or maybe some icon?
                    }
                }
            }
        },
        renderMaster = {
            Children(stack = component.masterStack, animation = stackAnimation(fade())) {
                when (val child = it.instance) {
                    is IMasterDetailComponent.Master.ItemsList -> {
                        renderItemsList(child.component)
                    }
                }
            }

        }

    )
}