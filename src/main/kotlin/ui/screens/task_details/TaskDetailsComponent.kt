package ui.screens.task_details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent

class TaskDetailsComponent(
    taskID: String,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<Task>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<Task> by di.instance()


//    private val dialogNav = SlotNavigation<DialogConfig>()

//    override val dialogSlot: Value<ChildSlot<*, ITaskDetailsComponent.TaskDetailsDialog>> =
//        childSlot(
//            source = dialogNav,
//            // persistent = false, // Disable navigation state saving, if needed
//            handleBackButton = true, // Close the dialog on back button press
//        ) { config, childComponentContext ->
//            when (config) {
//                DialogConfig.None -> ITaskDetailsComponent.TaskDetailsDialog.NONE
//                is DialogConfig.UserPickerDialog -> ITaskDetailsComponent.TaskDetailsDialog.UserPickerDialog(
//                    component = UserPickerComponent(
//                        di,
//                        childComponentContext
//                    )
//                )
//            }
//        }


    override val item: Flow<Task> = repo.getByID(taskID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }

    override fun updateItem(item: Task) {
        scope.launch {
            repo.update(item)
        }
    }

    override fun removeItem(item: Task) {
        scope.launch {
            repo.remove(item)
        }
    }

//    override fun showUserPickerDialog() {
//        dialogNav.activate(DialogConfig.UserPickerDialog())
//    }
//
//    override fun dismissDialog() {
//        dialogNav.dismiss()
//    }

    @Parcelize
    private sealed class DialogConfig : Parcelable {

        @Parcelize
        class UserPickerDialog() : DialogConfig()

        @Parcelize
        object None : DialogConfig()

    }


}