package ui.screens.master_detail.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import domain.EntitiesList
import domain.Task
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode
import ui.screens.master_detail.IMasterComponent

@Composable
fun SettingsListUi(component: IMasterComponent<SettingsType>) {
    val settingsTypes by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    EntitiesListUi(
        settingsTypes,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<SettingsType> {
            override fun getPrimaryText(item: SettingsType) = item.title

            override fun getIconPath(item: SettingsType): String = item.pathToIcon
        },
        onItemClicked = { component.onItemClicked(it) }
    )
}