package ui.screens.tasks_list

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import domain.EntitiesList
import domain.Task
import domain.User
import kotlinx.coroutines.flow.Flow
import ui.dialogs.IDialogComponent

interface ITasksListComponent {
    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>

    /**
     * tasks available for user
     * (created by user or with user as a member or team tasks if user is a team admin)
     */
    val tasks: Flow<EntitiesList<Task>>

    /**
     * Show a dialog for creating new team
     */
    fun createNewTaskRequest()

    /**
     * Create new team and store it in database
     */
    fun createNewTask(name: String, creator: User?)

    /**
     * delete the team (if user is a team creator)
     */
    fun deleteTasks()


}