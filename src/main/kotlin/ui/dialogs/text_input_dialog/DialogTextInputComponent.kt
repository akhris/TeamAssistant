package ui.dialogs.text_input_dialog

import com.arkivanov.decompose.ComponentContext
import ui.dialogs.IDialogComponent

class DialogTextInputComponent(
    private val componentContext: ComponentContext,
    private val message: String,
    private val onDismissed: () -> Unit,
) : IDialogComponent.ITextInputDialogComponent, ComponentContext by componentContext {
    override fun onDismiss() {
        onDismissed()
    }
}