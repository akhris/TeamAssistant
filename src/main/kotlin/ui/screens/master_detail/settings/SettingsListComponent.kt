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
) : IMasterComponent<SettingsSection>, BaseComponent(componentContext) {

    override val items: Flow<EntitiesList<SettingsSection>> = flowOf(
        EntitiesList.NotGrouped(
            items = listOfNotNull(
                SettingsSection.getSettingsSectionByID(SettingsSection.DBSettingsID),
                SettingsSection.getSettingsSectionByID(SettingsSection.APPSettingsID)
            )
        )
    )

    override fun onItemDelete(item: SettingsSection) {

    }

    override fun onAddNewItem(item: SettingsSection) {

    }

    override fun onItemClicked(item: SettingsSection) {
        onSettingsSelected(item.id)
    }

    override val filterSpecs: Flow<List<FilterSpec>>? = null

}