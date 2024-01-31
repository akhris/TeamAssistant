package domain.settings

import domain.IEntity

data class Setting(
    override val id: String,
    val value: String,
) : IEntity {
    companion object {
        //types:
        const val TYPE_BOOLEAN = "setting.type.boolean"
        const val TYPE_STRING = "setting.type.string"
        const val TYPE_PATH = "setting.type.path"
    }
}

sealed class SettingType {
    object String : SettingType()

    object Boolean : SettingType() {
        fun getValue(setting: Setting): kotlin.Boolean = setting.value.toBoolean()
    }
    object Path : SettingType()
}


data class SettingWrapper(
    val setting: Setting,
    val name: String,
    val description: String,
    val isHidden: Boolean,
)
