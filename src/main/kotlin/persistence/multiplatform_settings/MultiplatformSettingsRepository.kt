package persistence.multiplatform_settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings
import domain.ISettingsRepository
import domain.settings.ISettingMapper
import domain.settings.Setting
import utils.log

@OptIn(ExperimentalSettingsApi::class)
class MultiplatformSettingsRepository(
    private val preferencesSettings: SuspendSettings,
    private val mapper: ISettingMapper,
) : ISettingsRepository {


    override suspend fun getSetting(id: String): Setting? {
        val value = preferencesSettings.getStringOrNull(id)

        log(value ?: "no value", "got setting for id: $id in $this")
        return value?.let {
            mapper.map(id = id, value = it)
        }
    }


    // TODO: return stringsetting or string? how to get default value and description, e.t.c.
    //  see example:
    //  https://github.com/russhwolf/multiplatform-settings/blob/main/sample/shared/src/commonMain/kotlin/com/russhwolf/settings/example/SettingsRepository.kt


    override suspend fun update(setting: Setting) {
        preferencesSettings.putString(key = setting.id, value = setting.stringValue)
    }

    override suspend fun insert(setting: Setting) {
        preferencesSettings.putString(key = setting.id, value = setting.stringValue)
    }

    override suspend fun insert(settings: List<Setting>) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(entity: Setting) {
        TODO("Not yet implemented")
    }



}