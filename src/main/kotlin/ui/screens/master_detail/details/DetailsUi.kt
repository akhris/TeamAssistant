package ui.screens.master_detail.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import domain.EntitiesList
import domain.IEntity

@Composable
fun <T> DetailsUi(
    component: IDetailsComponent<T>,
    renderItemDetails: @Composable (item: T) -> Unit
) {
    val item by remember(component) { component.item }.collectAsState(null)

    item?.let {
        renderItemDetails(it)
    }
}