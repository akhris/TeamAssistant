package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import kotlinx.coroutines.delay
import utils.oppositeColor

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

        val listModifier = remember(filterPanelSize, bottomPanelSize) {
            Modifier.padding(
                top = filterPanelSize.height.dp,
                bottom = bottomPanelSize.height.dp
            )
        }

        RenderEntitiesList(
            modifier = listModifier,
            entitiesList = list,
            selection = selection,
            selectMode = selectMode,
            itemRenderer = itemRenderer,
            onItemClicked = { item ->
                when (selectMode) {
                    SelectMode.SINGLE -> {
                        selection = if (selection.contains(item)) {
                            listOf()
                        } else {
                            listOf(item)
                        }
                    }

                    SelectMode.MULTIPLE -> {
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
private fun <T> RenderEntitiesList(
    modifier: Modifier = Modifier,
    entitiesList: EntitiesList<T>,
    selection: List<T>,
    selectMode: SelectMode,
    itemRenderer: ItemRenderer<T>,
    onItemClicked: ((T) -> Unit)? = null,
    startOffset: Dp = 0.dp,
    topOffset: IntSize? = null,
    bottomOffset: IntSize? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (entitiesList) {
            is EntitiesList.Grouped -> RenderGroupedList(
                list = entitiesList,
                selection, selectMode, itemRenderer, onItemClicked, startOffset, topOffset, bottomOffset
            )

            is EntitiesList.NotGrouped -> RenderNotGroupedList(
                list = entitiesList.items,
                selection, selectMode, itemRenderer, onItemClicked, startOffset, topOffset, bottomOffset
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> ColumnScope.RenderGroupedList(
    list: EntitiesList.Grouped<T>,
    selection: List<T>,
    selectMode: SelectMode,
    itemRenderer: ItemRenderer<T>,
    onItemClicked: ((T) -> Unit)? = null,
    startOffset: Dp = 0.dp,
    topOffset: IntSize? = null,
    bottomOffset: IntSize? = null,
) {

    list
        .items
        .forEach { groupedItem ->
            var isExpanded by remember { mutableStateOf(true) }

            //title
            groupedItem.parentItem?.let { pItem ->
                RenderListItem(
                    item = pItem,
                    selection = selection,
                    selectMode = selectMode, itemRenderer = itemRenderer, onItemClicked = onItemClicked,
                    trailing = if (groupedItem.items.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = {
                                    isExpanded = !isExpanded
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier.rotate(if (isExpanded) 0f else 180f),
                                        imageVector = Icons.Rounded.ArrowDropDown,
                                        contentDescription = "show/hide subtasks"
                                    )
                                }
                            )
                        }
                    } else null,
                    startOffset = startOffset
                )
            }
            //items:
            if (isExpanded)
                RenderEntitiesList(
                    entitiesList = groupedItem.items,
                    selection = selection,
                    selectMode = selectMode,
                    itemRenderer = itemRenderer,
                    onItemClicked = onItemClicked,
                    startOffset = startOffset + UiSettings.GroupedList.groupOffset,
                    topOffset = topOffset,
                    bottomOffset = bottomOffset
                )
        }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> ColumnScope.RenderNotGroupedList(
    list: List<T>,
    selection: List<T>,
    selectMode: SelectMode,
    itemRenderer: ItemRenderer<T>,
    onItemClicked: ((T) -> Unit)? = null,
    startOffset: Dp = 0.dp,
    topOffset: IntSize? = null,
    bottomOffset: IntSize? = null,
) {

    list.forEach { item ->
        RenderListItem(
            item = item,
            selection = selection,
            selectMode = selectMode,
            itemRenderer = itemRenderer,
            onItemClicked = onItemClicked,
            startOffset = startOffset
        )
        Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.onBackground.copy(alpha = 0.12f))

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> RenderListItem(
    item: T,
    selection: List<T>,
    selectMode: SelectMode,
    itemRenderer: ItemRenderer<T>,
    onItemClicked: ((T) -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    startOffset: Dp = 0.dp,
) {
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
            .padding(start = startOffset)
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
                {

                    Text(
                        style = MaterialTheme.typography.overline.copy(
                            background = itemRenderer.getOverlineBackgroundColor(item) ?: Color.Unspecified,
                        ),
                        text = it,
                        color = itemRenderer.getOverlineBackgroundColor(item)?.oppositeColor() ?: Color.Unspecified
                    )
                }
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
        }, trailing = trailing
    )
}


interface ItemRenderer<T> {
    fun getPrimaryText(item: T): String? = null
    fun getSecondaryText(item: T): String? = null
    fun getOverlineText(item: T): String? = null

    fun getOverlineBackgroundColor(item: T): Color? = null
    fun getIconPath(item: T): String? = null
    fun getIconTint(item: T): Color? = null
}