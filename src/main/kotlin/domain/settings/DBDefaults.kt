package domain.settings

object DBDefaults {
    fun getSettings(userID: String? = null): List<Setting> = listOfNotNull(
        userID?.let{
            Setting(
                settingID = SettingID.StringType(Setting.SETTING_ID_DB_CREATOR),
                name = "database creator",
                description = "id of user that created database file",
                value = it,
                isHidden = true
            )
        }


    )
}