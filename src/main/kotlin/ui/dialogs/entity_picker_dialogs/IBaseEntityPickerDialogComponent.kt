package ui.dialogs.entity_picker_dialogs

import com.arkivanov.decompose.value.Value
import domain.EntitiesList
import kotlinx.coroutines.flow.Flow
import ui.ItemRenderer
import ui.SelectMode

interface IBaseEntityPickerDialogComponent<T> {
    val items: Value<EntitiesList<T>>
    val hiddenItems: Set<T>

    val selectMode: SelectMode
    val itemRenderer: ItemRenderer<T>
    val title: String

    val initialSelection: List<T>

    fun onItemsSelected(items: List<T>)
}

