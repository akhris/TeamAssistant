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

        val defaults: List<Setting> = listOf(
            Setting(id = SETTING_ID_DB_PATH, value = AppFoldersManager.defaultDBFilePath.pathString)
        )
    }

    // APP-related Settings
    object APP {
        val defaults: List<Setting> = listOf()
    }

    val defaults: List<Setting> = DB.defaults.plus(APP.defaults)
}