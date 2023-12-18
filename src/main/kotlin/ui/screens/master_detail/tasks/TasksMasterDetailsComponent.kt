package ui.screens.master_detail.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.IRepositoryObservable
import domain.Task
import domain.Team
import domain.User
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.FABState
import ui.dialogs.DialogProperties
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.task_details.TaskDetailsComponent
import ui.screens.tasks_list.TasksListComponent
import utils.log
import java.time.LocalDateTime

class TasksMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<Task>(componentContext = componentContext,
        createMasterComponent = { componentContext: ComponentContext, onItemSelected: (String) -> Unit ->
            TasksListComponent(di = di, componentContext = componentContext, onTaskSelected = onItemSelected)
        },
        createDetailsComponent = {componentContext, itemID->
            TaskDetailsComponent(di = di, componentContext = componentContext, taskID = itemID)
        }
        ) {

    private val repo: IRepositoryObservable<Task> by di.instance()

    override fun getAddNewEntityDialogProperties(): DialogProperties =
        DialogProperties(
            title = "добавить задачу",
            hint = "имя задачи",
            OKButtonText = "добавить"
        )

    override val fabState: Value<FABState> = MutableValue(
        FABState.HIDDEN
//
//        FABState.VISIBLE(
//            iconPath = "vector/add_black_24dp.svg",
//            text = "добавить задачу",
//            description = ""
//        )
    )

    override fun onDialogOKClicked(text: String, user: User) {
        createNewTask(text, user)
    }

    private fun createNewTask(name: String, creator: User){
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