package ui.screens.task_details

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import domain.Task
import domain.User
import ui.dialogs.base_entity_picker_dialog.IBaseEntityPickerDialogComponent
import ui.screens.master_detail.IDetailsComponent

interface ITaskDetailsComponent : IDetailsComponent<Task> {
    val dialogSlot: Value<ChildSlot<*, TaskDetailsDialog>>

    fun showUserPickerDialog()
    fun dismissDialog()

    sealed class TaskDetailsDialog {
        object NONE : TaskDetailsDialog()

        class UserPickerDialog(val component: IBaseEntityPickerDialogComponent<User>) : TaskDetailsDialog()
    }
}