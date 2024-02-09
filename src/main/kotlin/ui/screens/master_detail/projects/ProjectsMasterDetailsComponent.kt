package ui.screens.master_detail.projects

import com.arkivanov.decompose.ComponentContext
import domain.IRepositoryObservable
import domain.Project
import domain.User
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.dialogs.DialogProperties
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.project_details.ProjectDetailsComponent
import ui.screens.projects_list.ProjectsListComponent
import utils.log
import java.time.LocalDateTime

class ProjectsMasterDetailsComponent(
    private val di: DI, componentContext: ComponentContext, dbPath: String,
    override val currentUser: User,
) :
    BaseMasterDetailsComponent<Project>(componentContext = componentContext,
        createMasterComponent = { context: ComponentContext, onItemSelected: (String) -> Unit ->
            ProjectsListComponent(
                di = di,
                componentContext = context,
                onItemSelected = onItemSelected,
                dpPath = dbPath,
                currentUser = currentUser
            )
        },
        createDetailsComponent = { context, itemID ->
            ProjectDetailsComponent(
                di = di,
                componentContext = context,
                projectID = itemID,
                dpPath = dbPath,
                currentUser = currentUser
            )
        }) {
    private val repo: IRepositoryObservable<Project> by di.instance()

    override fun getAddNewEntityDialogProperties(): DialogProperties =
        DialogProperties(
            title = "добавить проект",
            hint = "имя проекта",
            OKButtonText = "добавить"
        )

    override fun onDialogOKClicked(text: String, user: User) {
        createNewProject(text, user)
    }

    private fun createNewProject(name: String, creator: User?) {
        scope.launch {
            val newProject = Project(
                name = name,
                createdAt = LocalDateTime.now(),
                creator = creator
            )
            try {
                repo.insert(newProject)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }
}