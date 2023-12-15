package utils

import domain.valueobjects.Attachment

fun Attachment.getIconPath(): String = when (this) {
    is Attachment.Email -> "vector/email_black_24dp.svg"
    is Attachment.File -> "vector/description_black_24dp.svg"
    is Attachment.Folder -> "vector/folder_black_24dp.svg"
    is Attachment.InternetLink -> "vector/link_black_24dp.svg"
}