package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import domain.*
import domain.settings.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent

class SettingsDetailsComponent(
    settingsSection: SettingsSection,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<SettingsSection>, BaseComponent(componentContext) {

    private val repo: ISettingsRepository by di.instance()

    private val _item: MutableStateFlow<SettingsSection> = MutableStateFlow(settingsSection)

    override val item: Flow<SettingsSection> = _item


    fun updateSetting(setting: Setting){

    }

    override fun removeItem(item: SettingsSection) {

    }

    override fun updateItem(item: SettingsSection) {

    }

    init {
        //get settings from settings repository for given [settingsNavItem]
        scope.launch {
            when (settingsSection.id) {
                SettingsSection.DBSettingsID -> {
                    repo.query(listOf(Specification.QueryAll)).collect { s ->
                        _item.value = settingsSection.copy(settings = s)
                    }
                }

                SettingsSection.APPSettingsID -> {

                }
            }
        }
    }
}