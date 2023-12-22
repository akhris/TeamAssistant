package ui.dialogs.base_entity_picker_dialog

import domain.EntitiesList
import kotlinx.coroutines.flow.Flow
import ui.ItemRenderer

interface IBaseEntityPickerDialogComponent<T> {
    val items: Flow<EntitiesList<T>>

    val selectMode: SelectMode
    val itemRenderer: ItemRenderer<T>
    val title: String
}

sealed class SelectMode {
    object SINGLE : SelectMode()
    object MULTIPLE : SelectMode()
}