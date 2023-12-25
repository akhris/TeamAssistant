package ui.screens.task_details

import domain.Task
import domain.User
import ui.dialogs.entity_picker_dialogs.IBaseEntityPickerDialogComponent
import ui.screens.master_detail.IDetailsComponent

interface ITaskDetailsComponent : IDetailsComponent<Task> {
//    val dialogSlot: Value<ChildSlot<*, TaskDetailsDialog>>

//    fun showUserPickerDialog()
//    fun dismissDialog()

    sealed class TaskDetailsDialog {
        object NONE : TaskDetailsDialog()

        class UserPickerDialog(val component: IBaseEntityPickerDialogComponent<User>) : TaskDetailsDialog()
    }
}