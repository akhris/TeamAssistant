package ui.screens.master_detail.details

import com.arkivanov.decompose.ComponentContext
import domain.IEntity
import domain.IRepositoryObservable
import domain.RepoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.reflect.KClass

class DetailsComponent<T : IEntity>(
    repo: IRepositoryObservable<T>,
    entityID: String,
    componentContext: ComponentContext,
) : IDetailsComponent<T>, ComponentContext by componentContext {

    override val item: Flow<T> = repo.getByID(entityID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }
}