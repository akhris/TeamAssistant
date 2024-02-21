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

class TeamPickerComponent(
    isMultipleSelection: Boolean,
    override val initialSelection: List<Team>,
    private val onItemsPicked: (List<Team>) -> Unit,
    override val hiddenItems: Set<Team> = setOf(),
    di: DI,
    componentContext: ComponentContext,
    private val dbPath: String,
) : IBaseEntityPickerDialogComponent<Team>, BaseComponent(componentContext) {

    private val repo: (DatabaseArguments) -> IRepositoryObservable<Team> by di.factory()

    override val itemRenderer: ItemRenderer<Team> = object : ItemRenderer<Team> {
        override fun getPrimaryText(item: Team): String = item.name
        override fun getIconPath(item: Team): String = "vector/people_black_24dp.svg"
    }

    private val _items: MutableValue<EntitiesList<Team>> = MutableValue(EntitiesList.empty())
    override val items: Value<EntitiesList<Team>> = _items

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val title: String = "выбор команд"

    override fun onItemsSelected(items: List<Team>) {
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