package ui.dialogs.text_input_dialog

import com.arkivanov.decompose.ComponentContext

class DialogTextInputComponent (
    private val componentContext: ComponentContext,
    private val message: String,
    private val onDismissed: () -> Unit,
) : IDialogTextInputComponent, ComponentContext by componentContext {
    override fun onDismissClicked() {
        onDismissed()
    }
}