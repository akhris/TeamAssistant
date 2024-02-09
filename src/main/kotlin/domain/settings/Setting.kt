package domain.settings

import domain.IEntity
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import settings.Settings
import java.nio.file.Path
import kotlin.io.path.Path

sealed class Setting : IEntity {

    abstract val stringValue: String


    data class BooleanSetting(
        override val id: String,
        override val stringValue: String,
    ) : Setting() {
        constructor(id: String, value: Boolean) : this(id = id, stringValue = value.toString())

        val value: Boolean = stringValue.toBoolean()
    }

    data class PathSetting(
        override val id: String,
        override val stringValue: String,
    ) : Setting() {
        constructor(id: String, path: Path) : this(id = id, stringValue = path.toString())

        val value: Path = Path(stringValue)
    }

    data class StringSetting(
        override val id: String,
        override val stringValue: String,
    ) : Setting()


    data class ListSetting(
        override val id: String,
        override val stringValue: String,
    ) : Setting() {
        constructor(id: String, values: List<String>) : this(id = id, stringValue = Json.encodeToString(values))

        val value: List<String> = Json.decodeFromString(stringValue)
    }

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
        fun getValue(setting: Setting): kotlin.Boolean = setting.stringValue.toBoolean()
    }

    /**
     * @param extensions - file extensions without ".": zip, rar, 7z, realm
     */
    class Path(val extensions: List<kotlin.String> = listOf()) : SettingType()

    // TODO: object List
}

fun Setting.PathSetting.extensions(): List<String> {
    return when (this.id) {
        Settings.DB.SETTING_ID_DB_PATH -> listOf("realm")
        else -> listOf()
    }
}

