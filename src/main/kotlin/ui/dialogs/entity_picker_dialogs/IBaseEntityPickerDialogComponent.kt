package ui.dialogs.entity_picker_dialogs

import domain.EntitiesList
import kotlinx.coroutines.flow.Flow
import ui.ItemRenderer
import ui.SelectMode

interface IBaseEntityPickerDialogComponent<T> {
    val items: Flow<EntitiesList<T>>

    val selectMode: SelectMode
    val itemRenderer: ItemRenderer<T>
    val title: String

    val initialSelection: List<T>

    fun onItemsSelected(items: List<T>)
}

