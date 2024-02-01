package domain.application

import domain.ISettingsRepository
import domain.settings.Setting
import settings.Settings

/**
 * SettingsUseCase - class that combines settings repositories to provide easy access to setting by ID.
 * It determines storage place of Setting (by using appropriate repo: [dbSettingsRepo] or [appSettingsRepo]) and it's type.
 * @param dbSettingsRepo - repository of settings that are stored in database file
 * @param appSettingsRepo - repository of settings that are stored in local key-value storage
 */
class SettingsUseCase(
    private val dbSettingsRepo: ISettingsRepository,
    private val appSettingsRepo: ISettingsRepository,
) {
    /**
     * return appropriate Setting object by its ID
     * todo: use multiple values in single 'when' branch
     */
    suspend fun getSetting(settingID: String): Setting {
        val defSetting = Settings.defaults.find { it.id == settingID }
            ?: throw IllegalStateException("setting with id: $settingID was not found in defaults")
        val settingInRepo =
            when (settingID) {
                Settings.DB.SETTING_ID_DB_CREATOR -> dbSettingsRepo.getSetting(settingID)
                Settings.DB.SETTING_ID_DB_PATH -> appSettingsRepo.getSetting(settingID)
                else -> null
            }

        return settingInRepo ?: defSetting
    }

    /**
     * update appropriate Setting by it's ID
     * todo: use multiple values in single 'when' branch
     */
    suspend fun update(setting: Setting) {
        when (setting.id) {
            Settings.DB.SETTING_ID_DB_CREATOR -> dbSettingsRepo.update(setting)
            Settings.DB.SETTING_ID_DB_PATH -> appSettingsRepo.update(setting)
        }
    }

    // FIXME: it queries all settings that are STORED in settings repo
    //  but what if settings are not stored yet?
    //   maybe return not Flow but List<Setting> with default values for those that are not stored yet
    suspend fun getAllDBSettings(): List<Setting> {
        return Settings
            .DB
            .defaults
            .map { setting ->
                val loadedValue = dbSettingsRepo.getSetting(id = setting.id)
                loadedValue?.let {
                    setting.copy(value = it.value)
                } ?: setting
            }

//        return dbSettingsRepo.query(listOf(Specification.QueryAll))
    }

    // FIXME: it queries all settings that are STORED in settings repo
    //  but what if settings are not stored yet?
    //   maybe return not Flow but List<Setting> with default values for those that are not stored yet
    suspend fun getAllAPPSettings(): List<Setting> {
        return Settings
            .APP
            .defaults
            .map { setting ->
                val loadedValue = appSettingsRepo.getSetting(id = setting.id)
                loadedValue?.let {
                    setting.copy(value = it.value)
                } ?: setting
            }
    }
}