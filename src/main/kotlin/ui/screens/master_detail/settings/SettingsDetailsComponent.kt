package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.kodein.di.DI
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent

class SettingsDetailsComponent(
    settingsItem: SettingsItem,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<SettingsItem>, BaseComponent(componentContext) {
    override val item: Flow<SettingsItem> =
        flowOf(settingsItem)

    override fun removeItem(item: SettingsItem) {

    }

    override fun updateItem(item: SettingsItem) {

    }
}