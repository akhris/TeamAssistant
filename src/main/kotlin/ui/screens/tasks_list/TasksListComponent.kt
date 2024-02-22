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
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.instance
import settings.DatabaseArguments
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent
import ui.screens.master_detail.IMasterComponent
import utils.UserUtils
import utils.log
import java.time.LocalDateTime

class TasksListComponent(
    di: DI,
    componentContext: ComponentContext,
    private val onTaskSelected: (String) -> Unit,
    private val dpPath: String,
    override val currentUser: User,
) : IMasterComponent<Task>, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    private val userID = UserUtils.getUserID()

    private val repo: IRepositoryObservable<Task> by di.instance(arg = DatabaseArguments(path = dpPath))

    override val filterSpecs: Flow<List<FilterSpec>>? = repo.getFilterSpecs()

    override val items: Flow<EntitiesList<Task>> =
        repo
            .query(listOf(Specification.GetAllForUserID(userID)))
            .map { rawList ->
                when (rawList) {
                    is EntitiesList.Grouped -> rawList
                    is EntitiesList.NotGrouped -> rawList.groupByParents()
                }
            }


    override fun onAddNewItem(item: Task) {
        scope.launch {
            try {
                repo.insert(item)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }

    override fun onItemDelete(item: Task) {
        TODO("Not yet implemented")
    }

    override fun onItemClicked(item: Task) {
        onTaskSelected(item.id)
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