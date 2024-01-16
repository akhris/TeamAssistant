package domain.settings

import domain.IEntity

data class Setting(
    val settingID: SettingID,
    val name: String,
    val description: String,
    val isHidden: Boolean,
    val value: String,
) : IEntity {
    override val id: String = settingID.id

    companion object {
        //ids:
        const val SETTING_ID_DB_CREATOR = "setting.id.db_creator"
        const val SETTING_ID_DB_PATH = "setting.id.db_path"

        //types:
        const val TYPE_BOOLEAN = "setting.type.boolean"
        const val TYPE_STRING = "setting.type.string"
        const val TYPE_PATH = "setting.type.path"
    }
}


sealed class SettingID(val id: String, val type: String) {

    class StringType(id: String) : SettingID(id = id, type = Setting.TYPE_STRING)
    class BooleanType(id: String) : SettingID(id = id, type = Setting.TYPE_BOOLEAN)
    class PathType(id: String) : SettingID(id = id, type = Setting.TYPE_PATH)
}
