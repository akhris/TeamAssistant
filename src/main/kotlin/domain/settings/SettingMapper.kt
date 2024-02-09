package domain.settings

import kotlinx.serialization.json.Json
import settings.Settings
import kotlin.io.path.Path

object SettingMapper : ISettingMapper {
    override fun map(id: String, value: String): Setting {
        return when (id) {
            Settings.DB.SETTING_ID_DB_PATH -> Setting.PathSetting(id = id, stringValue = value)
            Settings.DB.SETTING_ID_DB_CREATOR -> Setting.StringSetting(id = id, stringValue = value)
            Settings.DB.SETTING_ID_DB_LAST_OPENED_PATHS -> Setting.ListSetting(
                id = id,
                stringValue = value
            )

            else -> throw IllegalArgumentException("Setting with id: $id was not found")

        }
    }
}