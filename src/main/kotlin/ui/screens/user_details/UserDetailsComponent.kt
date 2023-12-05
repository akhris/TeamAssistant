package ui.screens.user_details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import domain.IRepositoryObservable
import domain.Project
import domain.RepoResult
import domain.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import utils.UserUtils

class UserDetailsComponent(
    userID: String = UserUtils.getUserID(),
    di: DI,
    componentContext: ComponentContext
) : IUserDetailsComponent, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<User> by di.instance()

    override val item: Flow<User> = repo.getByID(userID).mapNotNull {
        when (it) {
            is RepoResult.InitialItem -> it.item
            is RepoResult.ItemInserted -> it.item
            is RepoResult.ItemRemoved -> it.item
            is RepoResult.ItemUpdated -> it.item
            is RepoResult.PendindObject -> it.item
        }
    }

    override fun updateUser(user: User) {
        scope.launch {
            repo.update(user)
        }
    }


}