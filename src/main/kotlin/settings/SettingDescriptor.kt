package settings

import domain.settings.ISettingDescriptor
import domain.settings.SettingType

class SettingDescriptor : ISettingDescriptor {
    override fun getTitle(id: String): String? = when (id) {
        Settings.DB.SETTING_ID_DB_PATH -> "путь к базе данных"
        Settings.DB.SETTING_ID_DB_CREATOR -> "создатель базы данных"
        else -> null
    }


    override fun getDescription(id: String): String? = when (id) {
        Settings.DB.SETTING_ID_DB_PATH -> "путь к базе данных"
        Settings.DB.SETTING_ID_DB_CREATOR -> "user ID пользователя, создавшего базу"
        else -> null
    }

    override fun getType(id: String): SettingType? = when (id) {
        Settings.DB.SETTING_ID_DB_PATH -> SettingType.Path
        Settings.DB.SETTING_ID_DB_CREATOR -> SettingType.String
        else -> null
    }
}