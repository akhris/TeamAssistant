package ui.screens.master_detail

import LocalCurrentUser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.TextInputDialogUi

@Composable
fun <T> MasterDetailsUi(
    component: IMasterDetailComponent<T>,
    renderItemsList: @Composable (IMasterComponent<T>) -> Unit,
    renderItemDetails: @Composable (IDetailsComponent<T>) -> Unit,
) {

    val currentUser = LocalCurrentUser.current

    BaseMasterDetailsUi(
        renderMaster = {
            Children(stack = component.masterStack, animation = stackAnimation(fade())) {
                when (val child = it.instance) {
                    is IMasterDetailComponent.Master.ItemsList -> {
                        renderItemsList(child.component)
                    }
                }
            }

        }
    ) {
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
    }

    val dialogSlotValue = remember(component) { component.dialogSlot }
    dialogSlotValue?.let { dsv ->
        val dialogSlot by remember(dsv) { dsv }.subscribeAsState()
        dialogSlot.child?.instance?.also { dialogComponent ->
            when (dialogComponent) {
                is IDialogComponent.ITextInputDialogComponent -> {
                    TextInputDialogUi(component = dialogComponent, onOkClicked = { newText ->
                        currentUser?.let { user ->
                            component.onDialogOKClicked(newText, user)
                        }
                    })
                }

                IDialogComponent.NONE -> {
                    //show nothing
                }
            }
        }
    }
}