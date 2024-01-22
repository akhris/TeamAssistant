package persistence.multiplatform_settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings
import domain.ISettingsRepository
import domain.ISpecification
import domain.settings.Setting
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class MultiplatformSettingsRepository(private val preferencesSettings: SuspendSettings) : ISettingsRepository {


    override suspend fun getStringSetting(id: String): Setting.StringSetting? {
        val value = preferencesSettings.getStringOrNull(id)
        return value?.let {
            Setting.StringSetting(id = id, value = it)
        }
    }

    override suspend fun getBooleanSetting(id: String): Setting.BooleanSetting? {
        val value = preferencesSettings.getBooleanOrNull(id)
        return value?.let {
            Setting.BooleanSetting(id = id, value = it)
        }
    }

    override suspend fun getPathSetting(id: String): Setting.PathSetting? {
        val value = preferencesSettings.getStringOrNull(id)
        return value?.let {
            Setting.PathSetting(id = id, value = it)
        }
    }


    // TODO: return stringsetting or string? how to get default value and description, e.t.c.
    //  see example:
    //  https://github.com/russhwolf/multiplatform-settings/blob/main/sample/shared/src/commonMain/kotlin/com/russhwolf/settings/example/SettingsRepository.kt


    override fun query(specifications: List<ISpecification>): Flow<List<Setting>> {
        TODO("Not yet implemented")
    }

    override suspend fun update(setting: Setting) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(setting: Setting) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(settings: List<Setting>) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(entity: Setting) {
        TODO("Not yet implemented")
    }

}