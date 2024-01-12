package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import domain.EntitiesList
import domain.FilterSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.kodein.di.DI
import ui.screens.BaseComponent
import ui.screens.master_detail.IMasterComponent

class SettingsListComponent(
    di: DI,
    componentContext: ComponentContext,
) : IMasterComponent<SettingsNavItem>, BaseComponent(componentContext) {

    override val items: Flow<EntitiesList<SettingsNavItem>> = flowOf(
        EntitiesList.NotGrouped(
            items = listOf(SettingsNavItem.DBSettings)
        )
    )

    override fun onItemDelete(item: SettingsNavItem) {

    }

    override fun onAddNewItem(item: SettingsNavItem) {

    }

    override fun onItemClicked(item: SettingsNavItem) {

    }

    override val filterSpecs: Flow<List<FilterSpec>>? = null

}