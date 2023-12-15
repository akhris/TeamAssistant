package domain.valueobjects

sealed class Attachment {

    abstract val name: String
    abstract val description: String

    data class File(
        val path: String = "",
        override val name: String = "",
        override val description: String = ""
    ) : Attachment()

    data class Folder(
        val path: String = "",
        override val name: String = "",
        override val description: String = ""
    ) : Attachment()

    data class InternetLink(
        val link: String = "",
        override val name: String = "",
        override val description: String = ""
    ) :
        Attachment()

    data class Email(
        val email: String = "",
        override val name: String = "",
        override val description: String = ""
    ) : Attachment()
}
