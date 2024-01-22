package domain.settings

interface ISettingDescriptor {
    fun getTitle(id: String) : String?
    fun getDescription(id: String) : String?

}