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

class TaskPickerComponent(
    isMultipleSelection: Boolean,
    override val initialSelection: List<Task>,
    private val onItemsPicked: (List<Task>) -> Unit,
    override val hiddenItems: Set<Task> = setOf(),
    di: DI,
    componentContext: ComponentContext,
    private val dbPath: String,
) : IBaseEntityPickerDialogComponent<Task>, BaseComponent(componentContext) {

    private val repo: (DatabaseArguments) -> IRepositoryObservable<Task> by di.factory()

    override val itemRenderer: ItemRenderer<Task> = object : ItemRenderer<Task> {
        override fun getPrimaryText(item: Task): String = item.name
        override fun getIconPath(item: Task): String = "vector/users/person_black_24dp.svg"
    }


    private val _items: MutableValue<EntitiesList<Task>> = MutableValue(EntitiesList.empty())
    override val items: Value<EntitiesList<Task>> = _items

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val title: String = "выбор проектов"

    override fun onItemsSelected(items: List<Task>) {
        onItemsPicked(items)
    }

    private suspend fun invalidateItems() {
        val itemsRepo = repo(DatabaseArguments(path = dbPath))
        val loadedItems = itemsRepo.queryBlocking(
            listOf(
                Specification.QueryAll,
                Specification.Filtered(
                    spec = FilterSpec.Values(
                        filteredValues = hiddenItems.map { it.id },
                        columnName = "_id"
                    ),
                    isFilteredOut = true
                ),
//                Specification.Filtered(
//                    spec = FilterSpec.Values(
//                        filteredValues = hiddenItems.map { it.id },
//                        columnName = "parentTask._id"
//                    ),
//                    isFilteredOut = true
//                )
            )
        )
        _items.value = loadedItems
    }

    init {
        scope.launch {
            invalidateItems()
        }
    }
}