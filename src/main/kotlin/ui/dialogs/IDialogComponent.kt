package ui.dialogs

sealed interface IDialogComponent {

    fun onDismiss()

    val properties: DialogProperties

    interface ITextInputDialogComponent : IDialogComponent {

    }

    object NONE : IDialogComponent{
        override fun onDismiss() {

        }

        override val properties: DialogProperties = DialogProperties()
    }
}

data class DialogProperties(
    val initialText: String = "",
    val title: String = "",
    val message: String = "",
    val OKButtonText: String = "ok",
    val hint: String = "",
)