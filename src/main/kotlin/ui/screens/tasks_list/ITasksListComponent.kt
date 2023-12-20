package ui.screens.tasks_list

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import domain.Task
import domain.User
import ui.dialogs.IDialogComponent
import ui.screens.master_detail.IMasterComponent

interface ITasksListComponent : IMasterComponent<Task> {
//    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>

    /**
     * delete the team (if user is a team creator)
     */
    fun deleteTasks()

}