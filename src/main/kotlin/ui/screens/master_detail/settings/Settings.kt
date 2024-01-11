package ui.screens.master_detail.settings

sealed class SettingsItem(val pathToIcon: String, val title: String) {
    object DBSettings :
        SettingsItem(pathToIcon = "vector/settings/storage_black_24dp.svg", title = "настройки базы данных")
}