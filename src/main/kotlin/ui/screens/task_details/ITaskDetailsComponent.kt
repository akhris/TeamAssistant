package ui.screens.task_details

import domain.Task
import domain.User
import kotlinx.coroutines.flow.Flow

interface ITaskDetailsComponent {
    fun updateTask(task: Task)
    fun removeTask(task: Task)

    val task: Flow<Task>
}