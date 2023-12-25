package ui.dialogs.entity_picker_dialogs

import domain.EntitiesList
import kotlinx.coroutines.flow.Flow
import ui.ItemRenderer

interface IBaseEntityPickerDialogComponent<T> {
    val items: Flow<EntitiesList<T>>

    val selectMode: SelectMode
    val itemRenderer: ItemRenderer<T>
    val title: String

    val initialSelection: List<T>

    fun onItemsSelected(items: List<T>)
}

sealed class SelectMode {
    object SINGLE : SelectMode()
    object MULTIPLE : SelectMode()
}