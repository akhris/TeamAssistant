package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.*
import domain.application.SettingsUseCase
import domain.settings.ISettingDescriptor
import domain.settings.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent
import utils.log

class SettingsDetailsComponent(
    private val settingsSection: SettingsSection,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<SettingsSection>, BaseComponent(componentContext) {

    // TODO: need to bind all settings repos (DB + APP + ...)
    //  maybe make different settings component (not details component)?
    private val settingsUseCase: SettingsUseCase by di.instance()

    private val _item: MutableStateFlow<SettingsSection> = MutableStateFlow(settingsSection)

    override val item: Flow<SettingsSection> = _item

    private val _settings: MutableValue<List<Setting>> = MutableValue(listOf())
    val settings: Value<List<Setting>> = _settings

    val settingDescriptor: ISettingDescriptor by di.instance()

    fun updateSetting(setting: Setting) {
        scope.launch {
            settingsUseCase.update(setting)
            invalidateSettings()
        }
    }

    override fun removeItem(item: SettingsSection) {

    }

    override fun updateItem(item: SettingsSection) {

    }

    private suspend fun invalidateSettings() {
        when (settingsSection.id) {
            SettingsSection.DBSettingsID -> {
                _settings.value = settingsUseCase.getAllDBSettings()
            }

            SettingsSection.APPSettingsID -> {
                _settings.value = settingsUseCase.getAllAPPSettings()
            }
        }

    }

    init {
        //get settings from settings repository for given [settingsNavItem]
        scope.launch {
            invalidateSettings()
        }
    }
}