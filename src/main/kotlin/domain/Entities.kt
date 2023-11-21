package domain

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
    val createdAt: LocalDateTime? = null
) : IEntity {
    override fun toString() = "$name $surname"
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
    val childTeams: List<Team> = listOf()
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
    val creatorID: String = "",
    val createdAt: LocalDateTime? = null,
    val state: State.Project? = null,
    val tasks: List<Task> = listOf()
) : IEntity


/**
 * Entity representing Task
 */
data class Task(
    override val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val projectID: String? = null,
    val creatorID: String? = null,
    val createdAt: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null,
    val mustBeDoneBefore: LocalDateTime? = null,
    val state: State.Task? = null,
    val users: List<String> = listOf(),
    val subtasks: List<SubTask> = listOf()
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

