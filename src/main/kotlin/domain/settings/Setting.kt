package domain.settings

import domain.IEntity

sealed class Setting(

) : IEntity {

    abstract override val id: String

    data class BooleanSetting(override val id: String, val value: Boolean) : Setting()
    data class StringSetting(override val id: String, val value: String) : Setting()
    data class PathSetting(override val id: String, val value: String) : Setting()

    companion object {
        //types:
        const val TYPE_BOOLEAN = "setting.type.boolean"
        const val TYPE_STRING = "setting.type.string"
        const val TYPE_PATH = "setting.type.path"
    }
}


data class SettingWrapper(
    val setting: Setting,
    val name: String,
    val description: String,
    val isHidden: Boolean,
)
