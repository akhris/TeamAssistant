package ui.screens.master_detail.settings

sealed class SettingsNavItem(val id: String, val pathToIcon: String, val title: String) {
    object DBSettings :
        SettingsNavItem(
            id = "settings.section.db",
            pathToIcon = "vector/settings/storage_black_24dp.svg",
            title = "настройки базы данных"
        )

    companion object {
        fun getSettingsNavItemByID(id: String): SettingsNavItem? {
            return when (id) {
                DBSettings.id -> DBSettings
                else -> null
            }
        }

    }
}