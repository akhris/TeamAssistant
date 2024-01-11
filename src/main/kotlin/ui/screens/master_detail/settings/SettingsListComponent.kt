package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import domain.EntitiesList
import domain.FilterSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.kodein.di.DI
import ui.screens.BaseComponent
import ui.screens.master_detail.IMasterComponent

class SettingsListComponent(
    di: DI,
    componentContext: ComponentContext,
) : IMasterComponent<SettingsItem>, BaseComponent(componentContext) {

    override val items: Flow<EntitiesList<SettingsItem>> = flowOf(
        EntitiesList.NotGrouped(
            items = listOf(SettingsItem.DBSettings)
        )
    )

    override fun onItemDelete(item: SettingsItem) {

    }

    override fun onAddNewItem(item: SettingsItem) {

    }

    override fun onItemClicked(item: SettingsItem) {

    }

    override val filterSpecs: Flow<List<FilterSpec>>? = null

}