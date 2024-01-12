package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent

class SettingsMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<SettingsNavItem>(
        createMasterComponent = { context, onItemSelected ->
            SettingsListComponent(di, context)
        },
        createDetailsComponent = { context, itemID ->
            val settingsNav = SettingsNavItem.getSettingsNavItemByID(itemID)
                ?: throw IllegalArgumentException("cannot find Settings section for given id: $itemID")
            SettingsDetailsComponent(settingsNavItem = settingsNav, di = di, componentContext = context)
        },
        componentContext = componentContext
    ) {
}