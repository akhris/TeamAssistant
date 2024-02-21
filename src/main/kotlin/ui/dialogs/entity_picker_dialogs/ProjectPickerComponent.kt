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

class ProjectPickerComponent(
    isMultipleSelection: Boolean,
    override val initialSelection: List<Project>,
    private val onItemsPicked: (List<Project>) -> Unit,
    override val hiddenItems: Set<Project> = setOf(),
    di: DI,
    componentContext: ComponentContext,
    private val dbPath: String,
)  : IBaseEntityPickerDialogComponent<Project>, BaseComponent(componentContext) {

    private val repo: (DatabaseArguments) -> IRepositoryObservable<Project> by di.factory()

    override val itemRenderer: ItemRenderer<Project> = object : ItemRenderer<Project> {
        override fun getPrimaryText(item: Project): String = item.name
        override fun getIconPath(item: Project): String =
            item.icon.ifEmpty { "vector/projects/rocket_black_24dp.svg" }
    }


    private val _items: MutableValue<EntitiesList<Project>> = MutableValue(EntitiesList.empty())
    override val items: Value<EntitiesList<Project>> = _items

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val title: String = "выбор проектов"

    override fun onItemsSelected(items: List<Project>) {
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