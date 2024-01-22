package domain.settings

object DBSettings {
    fun getDefaults(userID: String? = null): List<Setting> = listOfNotNull(
        userID?.let {
            Setting.StringSetting(
                id = SETTING_ID_DB_CREATOR,
                value = it
            )
        }
    )

    const val SETTING_ID_DB_PATH = "setting.id.db_path"
    const val SETTING_ID_DB_CREATOR = "setting.id.db_creator"

}