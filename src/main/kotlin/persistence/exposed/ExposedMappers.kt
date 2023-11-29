package persistence.exposed

import domain.*
import domain.valueobjects.State
import persistence.exposed.dto.*

fun EntityTeam.toTeam(): Team {
    return Team(
        id = this.id.value.toString(),
        name = this.name,
        createdAt = this.createdAt,
//        creator = this.roles.find { it.isCreator }?.user?.value?.toString(),
//        admins = this.roles.filter { it.isAdmin }.map { it.user.value.toString() },
//        members = this.roles.filter { it.isMember }.map { it.user.value.toString() },
        childTeams = this.childTeams.map { it.toTeam() }
    )
}

fun EntityUser.toUser(): User {
    return User(
        id = this.id.value.toString(),
        name = this.name,
        middleName = this.middleName,
        surname = this.surname,
        email = this.email,
        phoneNumber = this.phoneNumber,
        roomNumber = this.roomNumber,
        color = this.color
    )
}

fun EntityProject.toProject(): Project {
    return Project(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description,
        color = this.color,
//        creatorID = this.creatorID.value.toString(),
        createdAt = this.createdAt,
        state = this.states.maxBy { it.setAt }.toState(),
//        tasks = tasks.map { it.toTask() }
    )
}

fun EntityTask.toTask(): Task {
    return Task(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description,
//        project = this.project?.value?.toString(),
//        creatorID = this.users.find { it.isCreator }?.user?.value?.toString(),
        createdAt = this.createdAt,
        targetDate = this.doneBefore,
        subtasks = this.subTasks.map { it.toSubTask() }
    )
}

fun EntitySubTask.toSubTask(): SubTask {
    return SubTask(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        targetDate = this.targetDate,
        completedAt = this.completedAt
    )
}

fun EntityProjectState.toState(): State.Project {
    return when (this.state) {
        "state.project.active" -> State.Project.ACTIVE
        "state.project.archived" -> State.Project.ARCHIVED
        else -> throw IllegalStateException("unknown project state: ${this.state}")
    }
}