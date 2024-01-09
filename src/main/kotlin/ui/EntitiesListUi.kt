package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import kotlinx.coroutines.delay

@Composable
fun <T> EntitiesListUi(
    list: EntitiesList<T>,
    selectMode: SelectMode = SelectMode.NONSELECTABLE,
    initialSelection: List<T> = listOf(),
    itemRenderer: ItemRenderer<T>,
    onItemClicked: ((T) -> Unit)? = null,
    onAddItemClick: (() -> Unit)? = null,
    addItemText: String = "",
    onSelectionChanged: ((List<T>) -> Unit)? = null,
) {

    var selection by remember(initialSelection, selectMode) { mutableStateOf(initialSelection) }

    var filterPanelSize by remember { mutableStateOf(IntSize(0, 0)) }
    var bottomPanelSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box(modifier = Modifier.fillMaxHeight()) {
        RenderFilterPanel(modifier = Modifier.onSizeChanged {
            filterPanelSize = it
        })
        when (list) {
            is EntitiesList.Grouped -> RenderGroupedList(list, selectMode, itemRenderer)
            is EntitiesList.NotGrouped -> RenderNotGroupedList(
                list = list,
                selectMode = selectMode,
                itemRenderer = itemRenderer,
                selection = selection,
                onItemClicked = { item ->
                    when (selectMode) {
                        SelectMode.MULTIPLE, SelectMode.SINGLE -> {
                            selection = if (selection.contains(item)) {
                                selection.minus(item)
                            } else {
                                selection.plus(item)
                            }
                        }

                        SelectMode.NONSELECTABLE -> {
                            onItemClicked?.invoke(item)
                        }
                    }
                },
                topOffset = filterPanelSize,
                bottomOffset = bottomPanelSize,

                )
        }
        onAddItemClick?.let { oaic ->
            RenderBottomAction(modifier = Modifier.align(Alignment.BottomCenter).onSizeChanged {
                bottomPanelSize = it
            }, text = addItemText.ifEmpty { "добавить" }, onActionClicked = oaic)
        }
    }

    LaunchedEffect(selection) {
        if (selection == initialSelection)
            return@LaunchedEffect
        delay(UiSettings.Debounce.debounceTime)
        onSelectionChanged?.invoke(selection)
    }
}

@Composable
private fun RenderFilterPanel(
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxWidth().padding(8.dp), color = MaterialTheme.colors.background) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            //add filter buttons
        }
    }
}

@Composable
private fun RenderBottomAction(
    modifier: Modifier = Modifier,
    text: String = "",
    onActionClicked: (() -> Unit)? = null,
) {
    if (text.isNotEmpty()) {
        Surface(modifier = modifier.fillMaxWidth().padding(8.dp), color = MaterialTheme.colors.background) {
            Box(contentAlignment = Alignment.Center) {
                TextButton(
                    onClick = {
                        onActionClicked?.invoke()
                    }) {
                    Text(text)
                }
            }
        }
    }
}

@Composable
private fun <T> RenderGroupedList(
    list: EntitiesList.Grouped<T>,
    selectionMode: SelectMode,
    itemRenderer: ItemRenderer<T>,
    topOffset: IntSize? = null,
) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> RenderNotGroupedList(
    list: EntitiesList.NotGrouped<T>,
    selection: List<T>,
    selectMode: SelectMode,
    itemRenderer: ItemRenderer<T>,
    onItemClicked: ((T) -> Unit)? = null,
    topOffset: IntSize? = null,
    bottomOffset: IntSize? = null,
) {

    val modifier = remember(topOffset, bottomOffset) {
        Modifier.padding(top = topOffset?.height?.dp ?: 0.dp, bottom = bottomOffset?.height?.dp ?: 0.dp)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        list.items.forEach { item ->

            val iconRes =
                remember(item, selection) {
                    when (selectMode) {
                        SelectMode.MULTIPLE -> {
                            if (item in selection) {
                                "vector/check_box_black_24dp.svg"
                            } else {
                                "vector/check_box_outline_blank_black_24dp.svg"
                            }
                        }

                        SelectMode.NONSELECTABLE -> {
                            itemRenderer.getIconPath(item)
                        }

                        SelectMode.SINGLE -> {
                            if (item in selection) {
                                "vector/radio_button_checked_black_24dp.svg"
                            } else {
                                "vector/radio_button_unchecked_black_24dp.svg"
                            }
                        }
                    }
                }

            ListItem(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        onClick = {
                            onItemClicked?.invoke(item)
                        },
                        enabled = onItemClicked != null
                    ),
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

        }
    }
}


interface ItemRenderer<T> {
    fun getPrimaryText(item: T): String? = null
    fun getSecondaryText(item: T): String? = null
    fun getOverlineText(item: T): String? = null
    fun getIconPath(item: T): String? = null
    fun getIconTint(item: T): Color? = null
}