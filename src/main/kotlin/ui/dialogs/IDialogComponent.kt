package ui.dialogs

sealed interface IDialogComponent {

    fun onDismiss()

    interface ITextInputDialogComponent : IDialogComponent {

    }
}

