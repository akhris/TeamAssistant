package domain.valueobjects

import domain.User
import java.time.LocalDateTime

data class TaskMessage(
    val text: String = "",
    val user: User? = null,
    val createdAt: LocalDateTime? = null,
    val attachments: List<Attachment>
)