package ui.dialogs.entity_picker_dialogs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.*
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.factory
import settings.DatabaseArguments
import ui.ItemRenderer
import ui.SelectMode
import ui.screens.BaseComponent

class UserPickerComponent(
    isMultipleSelection: Boolean,
    override val initialSelection: List<User>,
    private val onItemsPicked: (List<User>) -> Unit,
    override val hiddenItems: Set<User> = setOf(),
    di: DI,
    componentContext: ComponentContext,
    private val dbPath: String,
)  : IBaseEntityPickerDialogComponent<User>, BaseComponent(componentContext) {

    private val repo: (DatabaseArguments) -> IRepositoryObservable<User> by di.factory()

    override val itemRenderer: ItemRenderer<User> = object : ItemRenderer<User> {
        override fun getPrimaryText(item: User): String = item.getInitials()
        override fun getIconPath(item: User): String = "vector/users/person_black_24dp.svg"
    }


    private val _items: MutableValue<EntitiesList<User>> = MutableValue(EntitiesList.empty())
    override val items: Value<EntitiesList<User>> = _items

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val title: String = "выбор проектов"

    override fun onItemsSelected(items: List<User>) {
        onItemsPicked(items)
    }

    private suspend fun invalidateItems() {
        val itemsRepo = repo(DatabaseArguments(path = dbPath))
        val loadedItems = itemsRepo.queryBlocking(listOf(Specification.QueryAll))
        _items.value = loadedItems
    }

    init {
        scope.launch {
            invalidateItems()
        }
    }
}