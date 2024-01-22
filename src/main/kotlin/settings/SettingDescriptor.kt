package settings

import domain.settings.DBSettings
import domain.settings.ISettingDescriptor

class SettingDescriptor : ISettingDescriptor {
    override fun getTitle(id: String): String? = when (id) {
        DBSettings.SETTING_ID_DB_PATH -> "путь к базе данных"
        DBSettings.SETTING_ID_DB_CREATOR -> "создатель базы данных"
        else -> null
    }


    override fun getDescription(id: String): String? = when (id) {
        DBSettings.SETTING_ID_DB_PATH -> "путь к базе данных"
        DBSettings.SETTING_ID_DB_CREATOR -> "user ID пользователя, создавшего базу"
        else -> null
    }
}