package ui.screens.project_details

import com.arkivanov.decompose.ComponentContext
import domain.IRepositoryObservable
import domain.Project
import domain.RepoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent

class ProjectDetailsComponent(
    projectID: String,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<Project>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<Project> by di.instance()


    override val item: Flow<Project> = repo.getByID(projectID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }

    override fun removeItem(item: Project) {
        scope.launch {
            repo.remove(item)
        }
    }

    override fun updateItem(item: Project) {
        scope.launch {
            repo.update(item)
        }
    }
}