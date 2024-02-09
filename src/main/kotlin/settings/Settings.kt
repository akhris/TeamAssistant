package settings

import domain.settings.Setting
import kotlin.io.path.pathString

/**
 * Settings object that contains settings IDs divided by purpose (not by setting storing place)
 */
object Settings {
    // DB-related Settings
    object DB {
        const val SETTING_ID_DB_PATH = "setting.id.db_path"
        const val SETTING_ID_DB_CREATOR = "setting.id.db_creator"
        const val SETTING_ID_DB_LAST_OPENED_PATHS = "setting.id.db_last_opened_paths"

        val defaults: List<Setting> = listOf(
            Setting.PathSetting(id = SETTING_ID_DB_PATH, stringValue = AppFoldersManager.defaultDBFilePath.pathString),
            Setting.StringSetting(id = SETTING_ID_DB_CREATOR, stringValue = "")
        )
    }

    // APP-related Settings
    object APP {
        val defaults: List<Setting> = listOf()
    }

    val defaults: List<Setting> = DB.defaults.plus(APP.defaults)
}