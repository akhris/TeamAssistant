package domain

import domain.valueobjects.Attachment
import domain.valueobjects.State
import java.time.LocalDateTime
import java.util.*

interface IEntity {
    val id: String
}

/**
 * Entity representing user
 */
data class User(
    override val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val middleName: String = "",
    val surname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val roomNumber: String = "",
    val color: Int? = null,
    val createdAt: LocalDateTime? = null,
    val avatar: String = "",
    val lastOnline: LocalDateTime? = null,
    val isPinned: Boolean = false
) : IEntity {
    override fun toString() = "$name $surname"

    fun getInitials(): String {
        val builder = StringBuilder()
        if (name.isNotEmpty()) {
            builder.append(name.first().uppercase())
            builder.append(".")
        }
        if (middleName.isNotEmpty()) {
            if (builder.isNotEmpty()) {
                builder.append(" ")
            }
            builder.append(middleName.first().uppercase())
            builder.append(".")
        }
        if (surname.isNotEmpty()) {
            if (builder.isNotEmpty()) {
                builder.append(" ")
            }
            builder.append(surname.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
        }
        return builder.toString()
    }
}

/**
 * Entity representing a team
 */
data class Team(
    override val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val creator: User? = null,
    val parentTeamID: String? = null,
    val admins: List<User> = listOf(),
    val members: List<User> = listOf(),
    val createdAt: LocalDateTime? = null,
    val childTeams: List<Team> = listOf(),
    val isPinned: Boolean = false
) : IEntity {
    override fun toString() = name
}

/**
 * Entity representing Project that team works on currently.
 * Project may contain tasks.
 * Project may contain additional information
 */
data class Project(
    override val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val color: Int? = null,
    val creator: User? = null,
    val createdAt: LocalDateTime? = null,
    val state: State.Project? = null,
//    val tasks: List<Task> = listOf(),
    val teams: List<Team> = listOf(),
    val isPinned: Boolean = false
) : IEntity


/**
 * Entity representing Task
 */
data class Task(
    override val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val project: Project? = null,
    val creator: User? = null,
    val createdAt: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null,
    val targetDate: LocalDateTime? = null,
    val state: State.Task? = null,
    val users: List<User> = listOf(),
    val subtasks: List<SubTask> = listOf(),
    val attachments: List<Attachment> = listOf(),
    val isPinned: Boolean = false,
    val priority: Int = 1
) : IEntity {
    override fun toString() = name
}


data class SubTask(
    override val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val createdAt: LocalDateTime? = null,
    val targetDate: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null
) : IEntity

