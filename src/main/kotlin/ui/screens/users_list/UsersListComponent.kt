package ui.screens.users_list

import com.arkivanov.decompose.ComponentContext
import domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import settings.DatabaseArguments
import ui.screens.BaseComponent
import ui.screens.master_detail.IMasterComponent
import utils.log

class UsersListComponent(
    di: DI,
    componentContext: ComponentContext,
    private val onItemSelected: (String) -> Unit,
    dpPath: String,
    override val currentUser: User
) : IMasterComponent<User>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<User> by di.instance(arg = DatabaseArguments(path = dpPath))


    override val items: Flow<EntitiesList<User>> = repo.query(listOf(Specification.QueryAll))

    override val filterSpecs: Flow<List<FilterSpec>>? = repo.getFilterSpecs()

    override fun onAddNewItem(item: User) {
        scope.launch {
            try {
                repo.insert(item)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }

    override fun onItemClicked(item: User) {
        onItemSelected(item.id)
    }

    override fun onItemDelete(item: User) {
        TODO("Not yet implemented")
    }
}