package ui.screens.master_detail.master

import com.arkivanov.decompose.ComponentContext
import domain.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import utils.UserUtils

class MasterComponent<T : IEntity>(
    repo: IRepositoryObservable<T>,
    componentContext: ComponentContext,
    private val onItemSelected: (id: String) -> Unit,
) : IMasterComponent<T>, BaseComponent(componentContext) {

    private val userID = UserUtils.getUserID()
    override val items: Flow<EntitiesList<T>> = repo.query(listOf(Specification.GetAllForUserID(userID)))

    override fun onItemClicked(item: T) {
        onItemSelected(item.id)
    }

   }