package ui.screens.project_details

import com.arkivanov.decompose.ComponentContext
import domain.IRepositoryObservable
import domain.Project
import domain.RepoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent

class ProjectDetailsComponent(
    projectID: String,
    di: DI,
    componentContext: ComponentContext,
) : IProjectDetailsComponent, BaseComponent(componentContext) {

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
}