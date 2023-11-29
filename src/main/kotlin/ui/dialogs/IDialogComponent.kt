package ui.dialogs

sealed interface IDialogComponent {

    fun onDismiss()

    val title: String
    val message: String
    val OKButtonText: String
    val initialText: String
    interface ITextInputDialogComponent : IDialogComponent {
        val hint: String
    }
}

