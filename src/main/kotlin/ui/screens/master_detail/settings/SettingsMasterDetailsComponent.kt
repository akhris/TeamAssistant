package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import domain.User
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent

class SettingsMasterDetailsComponent(
    private val di: DI, componentContext: ComponentContext, dbPath: String,
    override val currentUser: User,
) :
    BaseMasterDetailsComponent<SettingsSection>(
        createMasterComponent = { context, onItemSelected ->
            SettingsListComponent(di, context, onItemSelected, dpPath = dbPath, currentUser = currentUser)
        },
        createDetailsComponent = { context, itemID ->
            val settingsNav = SettingsSection.getSettingsSectionByID(itemID)
                ?: throw IllegalArgumentException("cannot find Settings section for given id: $itemID")
            SettingsDetailsComponent(
                settingsSection = settingsNav,
                di = di,
                componentContext = context,
                dpPath = dbPath,
                currentUser = currentUser
            )
        },
        componentContext = componentContext
    ) {
}