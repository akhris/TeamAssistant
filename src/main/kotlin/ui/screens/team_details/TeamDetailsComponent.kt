package ui.screens.team_details

import com.arkivanov.decompose.ComponentContext
import domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent

class TeamDetailsComponent(teamID: String, di: DI, componentContext: ComponentContext) : ITeamDetailsComponent, BaseComponent(componentContext) {
    private val repo: IRepositoryObservable<Team> by di.instance()

    override val item: Flow<Team> = repo.getByID(teamID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }
}