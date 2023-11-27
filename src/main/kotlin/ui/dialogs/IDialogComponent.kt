package ui.dialogs

sealed interface IDialogComponent {

    fun onDismiss()

    val title: String
    val message: String
    val OKButtonText: String
    interface ITextInputDialogComponent : IDialogComponent {
        val hint: String
    }
}

