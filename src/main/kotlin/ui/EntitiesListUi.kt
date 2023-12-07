package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.EntitiesList

@Composable
fun <T> EntitiesListUi(
    list: EntitiesList<T>,
    selectableMode: SelectableMode = SelectableMode.NonSelectable,
    itemRenderer: ItemRenderer<T>,
) {
    when (list) {
        is EntitiesList.Grouped -> renderGroupedList(list, selectableMode, itemRenderer)
        is EntitiesList.NotGrouped -> renderNotGroupedList(list, selectableMode, itemRenderer)
    }
}

@Composable
private fun <T> renderGroupedList(
    list: EntitiesList.Grouped<T>,
    selectableMode: SelectableMode,
    itemRenderer: ItemRenderer<T>,
) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> renderNotGroupedList(
    list: EntitiesList.NotGrouped<T>,
    selectableMode: SelectableMode,
    itemRenderer: ItemRenderer<T>,
) {

    Column {
        list.items.forEach { item ->
            Card {
                ListItem(
                    modifier = Modifier.padding(4.dp),
                    text = {
                        Text(text = itemRenderer.getPrimaryText(item))
                    }, secondaryText = itemRenderer.getSecondaryText(item).let {
                        if (it.isEmpty()) {
                            null
                        } else {
                            {
                                Text(it)
                            }
                        }
                    }, overlineText = itemRenderer.getOverlineText(item).let {
                        if (it.isEmpty()) {
                            null
                        } else {
                            {
                                Text(it)
                            }
                        }
                    }, icon = {})
            }
        }
    }
}


sealed class SelectableMode {
    object NonSelectable : SelectableMode()
    class SingleSelection<T>(initialSelection: T, val onItemSelected: (T) -> Unit) : SelectableMode()
    class MultiSelectable<T>(initialSelection: List<T>, val onItemsSelected: (List<T>) -> Unit) : SelectableMode()

}

interface ItemRenderer<T> {
    fun getPrimaryText(item: T): String
    fun getSecondaryText(item: T): String
    fun getOverlineText(item: T): String
}