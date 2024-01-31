package ui.screens.master_detail.settings

import domain.settings.Setting
import settings.Settings

/**
 * Settings section
 */
data class SettingsSection(
    val id: String,
    val pathToIcon: String,
    val title: String,
    val settings: List<Setting>,
) {

    companion object {

        const val DBSettingsID = "settings.section.db"
        const val APPSettingsID = "settings.section.app"

        val SETTINGS_SECTION_DB_IDS: List<String> = listOf(
            Settings.DB.SETTING_ID_DB_PATH
        )

        val SETTINGS_SECTION_APP_IDS: List<String> = listOf(

        )

        fun getSettingsSectionByID(id: String, settings: List<Setting> = listOf()): SettingsSection? {
            return when (id) {
                DBSettingsID -> SettingsSection(
                    id = id,
                    pathToIcon = "vector/settings/storage_black_24dp.svg",
                    title = "база данных",
                    settings = settings
                )

                APPSettingsID -> SettingsSection(
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