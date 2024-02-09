package domain.settings

interface ISettingMapper {
    fun map(id: String, value: String): Setting
}

