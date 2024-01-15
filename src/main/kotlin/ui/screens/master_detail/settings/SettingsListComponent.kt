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
    private val onSettingsSelected: (String) -> Unit,
) : IMasterComponent<SettingsType>, BaseComponent(componentContext) {

    override val items: Flow<EntitiesList<SettingsType>> = flowOf(
        EntitiesList.NotGrouped(
            items = listOfNotNull(
                SettingsType.getSettingsNavItemByID(SettingsType.DBSettingsID),
                SettingsType.getSettingsNavItemByID(SettingsType.APPSettingsID)
            )
        )
    )

    override fun onItemDelete(item: SettingsType) {

    }

    override fun onAddNewItem(item: SettingsType) {

    }

    override fun onItemClicked(item: SettingsType) {
        onSettingsSelected(item.id)
    }

    override val filterSpecs: Flow<List<FilterSpec>>? = null

}