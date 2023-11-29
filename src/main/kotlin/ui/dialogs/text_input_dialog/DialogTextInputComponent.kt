package ui.dialogs.text_input_dialog

import com.arkivanov.decompose.ComponentContext
import ui.dialogs.IDialogComponent

class DialogTextInputComponent(
    private val componentContext: ComponentContext,
    private val onDismissed: () -> Unit,
    override val initialText: String = "",
    override val title: String = "",
    override val message: String = "",
    override val OKButtonText: String = "ok",
    override val hint: String = "",
) : IDialogComponent.ITextInputDialogComponent, ComponentContext by componentContext {
    override fun onDismiss() {
        onDismissed()
    }
}