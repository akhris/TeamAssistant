package ui.screens.master_detail.tasks

import com.arkivanov.decompose.ComponentContext
import domain.IRepositoryObservable
import domain.Task
import domain.User
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.dialogs.DialogProperties
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.task_details.TaskDetailsComponent
import ui.screens.tasks_list.TasksListComponent
import utils.log
import java.time.LocalDateTime

class TasksMasterDetailsComponent(
    private val di: DI,
    componentContext: ComponentContext,
    dbPath: String,
    override val currentUser: User,
) :
    BaseMasterDetailsComponent<Task>(componentContext = componentContext,
        createMasterComponent = { componentContext: ComponentContext, onItemSelected: (String) -> Unit ->
            TasksListComponent(
                di = di,
                componentContext = componentContext,
                onTaskSelected = onItemSelected,
                dpPath = dbPath,
                currentUser = currentUser
            )
        },
        createDetailsComponent = { componentContext, itemID ->
            TaskDetailsComponent(
                di = di,
                componentContext = componentContext,
                taskID = itemID,
                dpPath = dbPath,
                currentUser = currentUser
            )
        }
    ) {

    private val repo: IRepositoryObservable<Task> by di.instance()

    override fun getAddNewEntityDialogProperties(): DialogProperties =
        DialogProperties(
            title = "добавить задачу",
            hint = "имя задачи",
            OKButtonText = "добавить"
        )

    override fun onDialogOKClicked(text: String, user: User) {
        createNewTask(text, user)
    }

    private fun createNewTask(name: String, creator: User) {
        scope.launch {
            val newTask = Task(
                name = name,
                createdAt = LocalDateTime.now(),
                creator = creator
            )

            try {
                repo.insert(newTask)
            } catch (e: Throwable) {
                log("error during saving new task: ")
                log(e.localizedMessage)
            }
        }
    }

}