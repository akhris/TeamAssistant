package ui.screens.master_detail.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import domain.EntitiesList
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode
import ui.screens.master_detail.IMasterComponent

@Composable
fun SettingsListUi(component: IMasterComponent<SettingsSection>) {
    val settingsTypes by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    EntitiesListUi(
        settingsTypes,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<SettingsSection> {
            override fun getPrimaryText(item: SettingsSection) = item.title

            override fun getIconPath(item: SettingsSection): String = item.pathToIcon
        },
        onItemClicked = { component.onItemClicked(it) }
    )
}