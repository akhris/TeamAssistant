package ui.screens.master_detail.settings

import domain.Setting

data class SettingsType(
    val id: String,
    val pathToIcon: String,
    val title: String,
    val settings: List<Setting>,
) {


//    class DBSettings(override val settings: List<Setting> = listOf()) :
//        SettingsType(
//            id = "settings.section.db",
//            pathToIcon = "vector/settings/storage_black_24dp.svg",
//            title = "база данных"
//        )
//
//    class APPSettings(override val settings: List<Setting> = listOf()) :
//        SettingsType(
//            id = "settings.section.app",
//            pathToIcon = "vector/settings_black_24dp.svg",
//            title = "приложение"
//        )

    companion object {

        const val DBSettingsID = "settings.section.db"
        const val APPSettingsID = "settings.section.app"

        fun getSettingsNavItemByID(id: String, settings: List<Setting> = listOf()): SettingsType? {
            return when (id) {
                DBSettingsID -> SettingsType(
                    id = id,
                    pathToIcon = "vector/settings/storage_black_24dp.svg",
                    title = "база данных",
                    settings = settings
                )

                APPSettingsID -> SettingsType(
                    id = id,
                    pathToIcon = "vector/settings_black_24dp.svg",
                    title = "приложение",
                    settings = settings
                )
                else -> null
            }
        }

    }
}