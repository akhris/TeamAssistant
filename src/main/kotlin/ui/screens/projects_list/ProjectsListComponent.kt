package ui.screens.projects_list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import settings.DatabaseArguments
import ui.screens.master_detail.IMasterComponent
import utils.UserUtils
import utils.log

class ProjectsListComponent(
    di: DI,
    componentContext: ComponentContext,
    private val onItemSelected: (String) -> Unit,
    dpPath: String,
    override val currentUser: User
) : IMasterComponent<Project>, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    private val userID = UserUtils.getUserID()

    private val repo: IRepositoryObservable<Project> by di.instance(arg = DatabaseArguments(path = dpPath))

    override val filterSpecs: Flow<List<FilterSpec>>? = repo.getFilterSpecs()

    //
//    override val dialogSlot: Value<ChildSlot<*, IDialogComponent>> =
//        childSlot(
//            source = dialogNavigation,
//            // persistent = false, // Disable navigation state saving, if needed
//            handleBackButton = true, // Close the dialog on back button press
//        ) { config, childComponentContext ->
//            when (config) {
//                DialogConfig.NewProjectDialog ->
//                    DialogTextInputComponent(
//                        componentContext = childComponentContext,
//                        hint = "имя нового проекта",
//                        title = "добавить проект",
//                        OKButtonText = "добавить",
//                        onDismissed = dialogNavigation::dismiss
//                    )
//            }
//        }
    override val items: Flow<EntitiesList<Project>> = repo.query(listOf(Specification.GetAllForUserID(userID)))


    override fun onItemClicked(item: Project) {
        onItemSelected(item.id)
    }

    override fun onAddNewItem(item: Project) {
        scope.launch {
            try {
                repo.insert(item)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }


    override fun onItemDelete(item: Project) {
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
        object NewProjectDialog : DialogConfig()
    }


}