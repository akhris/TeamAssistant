package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import domain.EntitiesList

@Composable
fun <T> EntitiesListUi(
    list: EntitiesList<T>,
    selectionMode: SelectionMode<T> = SelectionMode.NonSelectable(),
    itemRenderer: ItemRenderer<T>,
    onAddItemClick: (() -> Unit)? = null,
) {

    var filterPanelSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box {
        RenderFilterPanel(modifier = Modifier.onSizeChanged {
            filterPanelSize = it
        }, onAddItemClick = onAddItemClick)
        when (list) {
            is EntitiesList.Grouped -> RenderGroupedList(list, selectionMode, itemRenderer)
            is EntitiesList.NotGrouped -> RenderNotGroupedList(list, selectionMode, itemRenderer, filterPanelSize)
        }
    }
}

@Composable
private fun RenderFilterPanel(
    modifier: Modifier = Modifier,
    onAddItemClick: (() -> Unit)? = null,
) {
    Surface(modifier = modifier.fillMaxWidth().padding(8.dp), color = MaterialTheme.colors.background) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            onAddItemClick?.let { oaiClick ->
                Icon(
                    modifier = Modifier.clickable { oaiClick() },
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "add item"
                )
            }
        }
    }
}

@Composable
private fun <T> RenderGroupedList(
    list: EntitiesList.Grouped<T>,
    selectionMode: SelectionMode<T>,
    itemRenderer: ItemRenderer<T>,
    topOffset: IntSize? = null,
) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> RenderNotGroupedList(
    list: EntitiesList.NotGrouped<T>,
    selectionMode: SelectionMode<T>,
    itemRenderer: ItemRenderer<T>,
    topOffset: IntSize? = null,
) {
    var selection by remember(selectionMode) {
        mutableStateOf(
            when (selectionMode) {
                is SelectionMode.MultiSelection<T> -> {
                    selectionMode.initialSelection
                }

                is SelectionMode.NonSelectable -> {
                    listOf()
                }

                is SelectionMode.SingleSelection<T> -> {
                    listOf(selectionMode.initialSelection)
                }
            }
        )
    }
    val modifier = remember(topOffset) {
        topOffset?.let {
            Modifier.padding(top = it.height.dp)
        } ?: Modifier
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        list.items.forEach { item ->

            val iconRes =
                remember(item, selection) {
                    when (selectionMode) {
                        is SelectionMode.MultiSelection -> {
                            if (item in selection) {
                                "vector/check_box_black_24dp.svg"
                            } else {
                                "vector/check_box_outline_blank_black_24dp.svg"
                            }
                        }

                        is SelectionMode.NonSelectable -> {
                            itemRenderer.getIconPath(item)
                        }

                        is SelectionMode.SingleSelection -> {
                            if (item in selection) {
                                "vector/radio_button_checked_black_24dp.svg"
                            } else {
                                "vector/radio_button_unchecked_black_24dp.svg"
                            }
                        }
                    }
                }

//            Card(
//                modifier = Modifier.clickable(
//                    onClick = {
//                        selectableMode.onItemClicked?.invoke(item)
//                    },
//                    enabled = selectableMode.onItemClicked != null
//                )
//            ) {
                ListItem(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable(
                            onClick = {
                                selectionMode.onItemClicked?.invoke(item)
                            },
                            enabled = selectionMode.onItemClicked != null
                        )
                    ,
                    text = {
                        Text(text = itemRenderer.getPrimaryText(item) ?: "")
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
            Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.onBackground.copy(alpha = 0.12f))
//            }
        }
    }
}


sealed class SelectionMode<T> {

    abstract val onItemClicked: ((T) -> Unit)?

    class NonSelectable<T>(override val onItemClicked: ((T) -> Unit)? = null) : SelectionMode<T>()
    class SingleSelection<T>(
        val initialSelection: T? = null, val onItemSelected: (T) -> Unit,
        override val onItemClicked: ((T) -> Unit)? = null,
    ) : SelectionMode<T>()

    class MultiSelection<T>(
        val initialSelection: List<T>, val onItemsSelected: (List<T>) -> Unit,
        override val onItemClicked: ((T) -> Unit)? = null,
    ) :
        SelectionMode<T>()

}

interface ItemRenderer<T> {
    fun getPrimaryText(item: T): String? = null
    fun getSecondaryText(item: T): String? = null
    fun getOverlineText(item: T): String? = null
    fun getIconPath(item: T): String? = null
    fun getIconTint(item: T): Color? = null
}