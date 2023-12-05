package ui.screens.task_details

import domain.Task
import ui.screens.master_detail.IDetailsComponent

interface ITaskDetailsComponent : IDetailsComponent<Task> {
    fun updateTask(task: Task)
    fun removeTask(task: Task)
}