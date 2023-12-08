package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.EntitiesList

@Composable
fun <T> EntitiesListUi(
    list: EntitiesList<T>,
    selectableMode: SelectableMode<T> = SelectableMode.NonSelectable(),
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
    selectableMode: SelectableMode<T>,
    itemRenderer: ItemRenderer<T>,
) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> renderNotGroupedList(
    list: EntitiesList.NotGrouped<T>,
    selectableMode: SelectableMode<T>,
    itemRenderer: ItemRenderer<T>,
) {
    var selection by remember(selectableMode) {
        mutableStateOf(
            when (selectableMode) {
                is SelectableMode.MultiSelectable<T> -> {
                    selectableMode.initialSelection
                }

                is SelectableMode.NonSelectable -> {
                    listOf()
                }

                is SelectableMode.SingleSelection<T> -> {
                    listOf(selectableMode.initialSelection)
                }
            }
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        list.items.forEach { item ->

            val iconRes =
                remember(item, selection) {
                    when (selectableMode) {
                        is SelectableMode.MultiSelectable -> {
                            if (item in selection) {
                                "vector/check_box_black_24dp.svg"
                            } else {
                                "vector/check_box_outline_blank_black_24dp.svg"
                            }
                        }

                        is SelectableMode.NonSelectable -> {
                            itemRenderer.getIconPath(item)
                        }

                        is SelectableMode.SingleSelection -> {
                            if (item in selection) {
                                "vector/radio_button_checked_black_24dp.svg"
                            } else {
                                "vector/radio_button_unchecked_black_24dp.svg"
                            }
                        }
                    }
                }

            Card(
                modifier = Modifier.clickable(
                    onClick = {
                        selectableMode.onItemClicked?.invoke(item)
                    },
                    enabled = selectableMode.onItemClicked != null
                )
            ) {
                ListItem(
                    modifier = Modifier.padding(4.dp),
                    text = {
                        Text(text = itemRenderer.getPrimaryText(item)?:"")
                    }, secondaryText = itemRenderer.getSecondaryText(item)?.let {
                        if (it.isEmpty()) {
                            null
                        } else {
                            { Text(it) }
                        }
                    }, overlineText = itemRenderer.getOverlineText(item)?.let {
                        if (it.isEmpty()) {
                            null
                        } else {
                            { Text(it) }
                        }
                    }, icon = iconRes?.let { icon ->
                        {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null,
                                tint = itemRenderer.getIconTint(item)
                                    ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                            )
                        }
                    })
            }
        }
    }
}


sealed class SelectableMode<T> {

    abstract val onItemClicked: ((T) -> Unit)?

    class NonSelectable<T>(override val onItemClicked: ((T) -> Unit)? = null) : SelectableMode<T>()
    class SingleSelection<T>(
        val initialSelection: T, val onItemSelected: (T) -> Unit,
        override val onItemClicked: ((T) -> Unit)? = null,
    ) : SelectableMode<T>()

    class MultiSelectable<T>(
        val initialSelection: List<T>, val onItemsSelected: (List<T>) -> Unit,
        override val onItemClicked: ((T) -> Unit)? = null,
    ) :
        SelectableMode<T>()

}

interface ItemRenderer<T> {
    fun getPrimaryText(item: T): String? = null
    fun getSecondaryText(item: T): String? = null
    fun getOverlineText(item: T): String? = null
    fun getIconPath(item: T): String? = null
    fun getIconTint(item: T): Color? = null
}