package ui.screens.tasks_list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent
import ui.screens.teams_list.TeamsListComponent
import utils.UserUtils
import utils.log
import java.time.LocalDateTime

class TasksListComponent(
    di: DI,
    componentContext: ComponentContext
) : ITasksListComponent, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    private val userID = UserUtils.getUserID()

    private val repo: IRepositoryObservable<Task> by di.instance()
    override val dialogSlot: Value<ChildSlot<*, IDialogComponent>> =
        childSlot(
            source = dialogNavigation,
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            when (config) {
                DialogConfig.NewTaskDialog ->
                    DialogTextInputComponent(
                        componentContext = childComponentContext,
                        hint = "имя новой задачи",
                        title = "добавить задачу",
                        OKButtonText = "добавить",
                        onDismissed = dialogNavigation::dismiss
                    )
            }
        }

    override val tasks: Flow<EntitiesList<Task>> = repo.query(listOf(Specification.GetAllForUserID(userID)))

    override fun createNewTaskRequest() {
        dialogNavigation.activate(DialogConfig.NewTaskDialog)
    }

    override fun createNewTask(name: String, creator: User?) {
        scope.launch {
            val newTask = Task(
                name = name,
                createdAt = LocalDateTime.now(),
                creator = creator
            )

            try {
                repo.insert(newTask)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }

    override fun deleteTasks() {
        TODO("Not yet implemented")
    }

    init {
        componentContext
            .lifecycle
            .subscribe(onDestroy = {
                scope.coroutineContext.cancelChildren()
            })


    }


    private sealed class DialogConfig() : Parcelable {

        @Parcelize
        object NewTaskDialog : DialogConfig()
    }


}