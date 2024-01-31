package persistence.multiplatform_settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings
import domain.ISettingsRepository
import domain.ISpecification
import domain.settings.Setting
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class MultiplatformSettingsRepository(private val preferencesSettings: SuspendSettings) : ISettingsRepository {


    override suspend fun getSetting(id: String): Setting? {
        val value = preferencesSettings.getStringOrNull(id)
        return value?.let {
            Setting(id = id, value = it)
        }
    }


    // TODO: return stringsetting or string? how to get default value and description, e.t.c.
    //  see example:
    //  https://github.com/russhwolf/multiplatform-settings/blob/main/sample/shared/src/commonMain/kotlin/com/russhwolf/settings/example/SettingsRepository.kt



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