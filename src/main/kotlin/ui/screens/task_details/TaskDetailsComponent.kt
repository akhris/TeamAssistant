package ui.screens.task_details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import domain.IRepositoryObservable
import domain.RepoResult
import domain.Task
import domain.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import utils.UserUtils

class TaskDetailsComponent(
    taskID: String,
    di: DI,
    componentContext: ComponentContext
) : ITaskDetailsComponent, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<Task> by di.instance()

    override val task: Flow<Task> = repo.getByID(taskID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }

    override fun removeTask(task: Task) {
        scope.launch {
            repo.remove(task)
        }
    }

    override fun updateTask(task: Task) {
        scope.launch {
            repo.update(task)
        }
    }

}