package ui.screens.master_detail.master

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import domain.EntitiesList
import domain.FilterSpec
import domain.IEntity

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun <T : IEntity> MasterUi(
    component: IMasterComponent<T>,
    renderListItem: @Composable (item: T) -> Unit,
) {

    val items by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    val filterSpecs = remember(component) { component.filterSpecs }?.collectAsState(listOf())
//    renderItemsList(items)

    var showFilterPanel by remember { mutableStateOf(false) }

    Column {
        // TODO: Add filtering/sorting panel here
        Row {
            filterSpecs?.let {
                IconButton(onClick = {
                    showFilterPanel = !showFilterPanel
                }, content = {
                    Icon(painterResource("vector/filter_alt_black_24dp.svg"), contentDescription = "filter values")
                })
            }
        }
        if (showFilterPanel) {
            filterSpecs?.let {
                it
                    .value
                    .forEach { fSpec ->
                        Text(text = fSpec.columnName)
                        when (fSpec) {
                            is FilterSpec.Range<*> -> TODO()
                            is FilterSpec.Values -> {
                                FlowRow {
                                    fSpec.filteredValues.forEach { fv ->
                                        FilterChip(
                                            selected = false,
                                            onClick = {},
                                            content = { Text(fv?.toString() ?: "no value") })
                                    }
                                }
                            }
                        }
                    }
            }
        }

        when (items) {
            is EntitiesList.Grouped -> RenderGroupedItems(items as EntitiesList.Grouped<T>)
            is EntitiesList.NotGrouped -> RenderNotGroupedItems(
                items = items as EntitiesList.NotGrouped<T>,
                renderListItem = renderListItem,
                onItemClick = { component.onItemClicked(it) }
            )
        }
    }
}

@Composable
fun <T> RenderNotGroupedItems(
    items: EntitiesList.NotGrouped<T>,
    renderListItem: @Composable (items: T) -> Unit,
    onItemClick: (T) -> Unit,
) {
    Column {
        items.items.forEach { item ->
            Box(modifier = Modifier.clickable {
                onItemClick(item)
            }) {
                renderListItem(item)
            }
        }
    }
}

@Composable
fun <T> RenderGroupedItems(items: EntitiesList.Grouped<T>) {

}
