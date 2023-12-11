package ui.dialogs.text_input_dialog

import com.arkivanov.decompose.ComponentContext
import ui.dialogs.DialogProperties
import ui.dialogs.IDialogComponent

class DialogTextInputComponent(
    private val componentContext: ComponentContext,
    private val onDismissed: () -> Unit,
    override val properties: DialogProperties = DialogProperties()
) : IDialogComponent.ITextInputDialogComponent, ComponentContext by componentContext {
    override fun onDismiss() {
        onDismissed()
    }
}

