package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent

class SettingsMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<SettingsSection>(
        createMasterComponent = { context, onItemSelected ->
            SettingsListComponent(di, context, onItemSelected)
        },
        createDetailsComponent = { context, itemID ->
            val settingsNav = SettingsSection.getSettingsSectionByID(itemID)
                ?: throw IllegalArgumentException("cannot find Settings section for given id: $itemID")
            SettingsDetailsComponent(settingsSection = settingsNav, di = di, componentContext = context)
        },
        componentContext = componentContext
    ) {
}