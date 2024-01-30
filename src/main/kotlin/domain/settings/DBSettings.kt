package domain.settings

import settings.Settings

object DBSettings {
    fun getDefaults(userID: String? = null): List<Setting> = listOfNotNull(
        userID?.let {
            Setting.StringSetting(
                id = Settings.DB.SETTING_ID_DB_CREATOR,
                value = it
            )
        }
    )


}