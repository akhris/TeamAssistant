package ui.screens.user_details

import com.arkivanov.decompose.ComponentContext
import domain.IRepositoryObservable
import domain.RepoResult
import domain.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import settings.DatabaseArguments
import ui.screens.BaseComponent
import utils.UserUtils

class UserDetailsComponent(
    userID: String = UserUtils.getUserID(),
    di: DI,
    componentContext: ComponentContext,
    dpPath: String,
    override val currentUser: User
) : IUserDetailsComponent, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<User> by di.instance(arg = DatabaseArguments(path = dpPath))

    override val item: Flow<User> = repo.getByID(userID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }

    override fun removeItem(item: User) {
        scope.launch {
            repo.remove(item)
        }
    }

    override fun updateItem(item: User) {
        scope.launch {
            repo.update(item)
        }
    }


}