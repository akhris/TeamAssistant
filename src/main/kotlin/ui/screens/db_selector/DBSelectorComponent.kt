package ui.screens.db_selector

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.IRepositoryObservable
import domain.ISettingsRepository
import domain.User
import domain.settings.Setting
import io.realm.kotlin.Realm
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.factory
import org.kodein.di.instance
import settings.DatabaseArguments
import settings.Settings
import ui.screens.BaseComponent
import utils.log
import kotlin.io.path.Path
import kotlin.io.path.exists

class DBSelectorComponent(
    di: DI,
    componentContext: ComponentContext,
    private val onPathSelected: (String) -> Unit,
) : IDBSelectorComponent, BaseComponent(componentContext) {

    private val appSettingsRepo: ISettingsRepository by di.instance(tag = "settings.local")

    private val realmFactory: (DatabaseArguments) -> Realm by di.factory()
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

    override fun setCurrentDBPath(path: String) {
        scope.launch {
            //1. close previous realm DB (if exists):
            val currentPath = currentDBPath.value
            if (currentPath.isNotEmpty() && Path(currentPath).exists() && currentPath != path) {
                //current path exists
                val previousRealm = realmFactory(DatabaseArguments(currentPath))
                log(previousRealm.toString(), "going to close previous realm database: ")
                previousRealm.close()
            }
            //2. create/init new realm database:
            val newRealm = realmFactory(DatabaseArguments(path))
            log(newRealm.toString(), "going to use realm database: ")
            appSettingsRepo.update(Settings.DB.DEFAULT_DB_PATH.copy(stringValue = path))
            //todo: add path to the top of last opened paths
            onPathSelected(path)
        }
    }

    init {
        scope.launch {
            invalidateCurrentDBPath()
            invalidateLastOpenedDBPaths()
        }
    }

}