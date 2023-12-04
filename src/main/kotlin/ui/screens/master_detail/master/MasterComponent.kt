package ui.screens.master_detail.master

import com.arkivanov.decompose.ComponentContext
import domain.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ui.screens.BaseComponent
import utils.UserUtils

class MasterComponent<T : IEntity>(
    repo: IRepositoryObservable<T>,
    componentContext: ComponentContext,
    private val onItemSelected: (id: String) -> Unit,
) : IMasterComponent<T>, BaseComponent(componentContext) {

    private val userID = UserUtils.getUserID()

    private val _filter: MutableStateFlow<Specification.Filtered> = MutableStateFlow(Specification.Filtered())

    override val filterSpecs: Flow<List<FilterSpec>>? = repo.getFilterSpecs()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val items: Flow<EntitiesList<T>> =
        _filter
            .flatMapLatest { filterSpec ->
                repo
                    .query(listOf(Specification.GetAllForUserID(userID), filterSpec))
            }


    override fun onItemClicked(item: T) {
        onItemSelected(item.id)
    }

}

