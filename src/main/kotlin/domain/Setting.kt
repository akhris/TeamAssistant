package domain

sealed class Setting() : IEntity {
    abstract val name: String
    abstract val description: String

    class BooleanSetting(
        override val id: String,
        override val name: String,
        override val description: String,
        val value: Boolean,
    ) : Setting()

    class StringSetting(
        override val id: String,
        override val name: String,
        override val description: String,
        val value: String,
    ) : Setting()

    companion object {
        const val SETTING_ID_DB_CREATOR = "setting.id.db_creator"

        const val TYPE_BOOLEAN = "setting.type.boolean"
        const val TYPE_STRING = "setting.type.string"
    }
}

fun Setting.getType(): String {
    return when (this) {
        is Setting.BooleanSetting -> Setting.TYPE_BOOLEAN
        is Setting.StringSetting -> Setting.TYPE_STRING
    }
}