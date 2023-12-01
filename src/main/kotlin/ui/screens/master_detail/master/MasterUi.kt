package ui.screens.master_detail.master

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import domain.EntitiesList
import domain.IEntity

@Composable
fun <T : IEntity> MasterUi(
    component: IMasterComponent<T>,
    renderListItem: @Composable (item: T) -> Unit,
) {

    val items by remember(component) { component.items }.collectAsState(EntitiesList.empty())

//    renderItemsList(items)

    // TODO: Add filtering/sorting panel here

    when (items) {
        is EntitiesList.Grouped -> RenderGroupedItems(items as EntitiesList.Grouped<T>)
        is EntitiesList.NotGrouped -> RenderNotGroupedItems(
            items = items as EntitiesList.NotGrouped<T>,
            renderListItem = renderListItem,
            onItemClick = { component.onItemClicked(it) }
        )
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
