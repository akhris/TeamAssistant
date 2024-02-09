package ui.screens.db_selector

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.ISettingsRepository
import domain.settings.Setting
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import settings.Settings
import ui.screens.BaseComponent

class DBSelectorComponent(
    di: DI,
    componentContext: ComponentContext,
    onPathSelected: (String) -> Unit
) : IDBSelectorComponent, BaseComponent(componentContext) {

    private val appSettingsRepo: ISettingsRepository by di.instance(tag = "settings.local")

    private val _currentDBPath: MutableValue<String> = MutableValue("")
    override val currentDBPath: Value<String> = _currentDBPath

    private val _lastOpenedDBPaths: MutableValue<List<String>> = MutableValue(listOf())

    override val lastOpenedDBPaths: Value<List<String>> = _lastOpenedDBPaths

    private suspend fun invalidateLastOpenedDBPaths() {
        val lastOpenedDBPathsSetting =
            appSettingsRepo.getSetting(Settings.DB.SETTING_ID_DB_LAST_OPENED_PATHS) as? Setting.ListSetting ?: return
        _lastOpenedDBPaths.value = lastOpenedDBPathsSetting.value
    }

    private suspend fun invalidateCurrentDBPath() {
        val currentDBPathSetting =
            appSettingsRepo.getSetting(Settings.DB.SETTING_ID_DB_PATH) as? Setting.PathSetting ?: return
        _currentDBPath.value = currentDBPathSetting.stringValue
    }

    init {
        scope.launch {
            invalidateCurrentDBPath()
            invalidateLastOpenedDBPaths()
        }
    }

}