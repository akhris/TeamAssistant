package persistence.exposed.dto

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class EntityProject(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityProject>(Tables.Projects)

    val name by Tables.Projects.name
    val description by Tables.Projects.description
    val color by Tables.Projects.color
    val createdAt by Tables.Projects.createdAt
    val creatorID by Tables.Projects.createdBy
    val states by EntityProjectState referrersOn Tables.ProjectStates.project
    val tasks by EntityTask optionalReferrersOn Tables.Tasks.project
}

class EntityProjectRole(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EntityProjectRole>(Tables.ProjectRoles)

    val project by EntityProject referencedOn Tables.ProjectRoles.project
    val isCreator by Tables.ProjectRoles.isCreator
    val isManager by Tables.ProjectRoles.isManager
}

class EntityProjectState(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EntityProjectState>(Tables.ProjectStates)

    val project by Tables.ProjectStates.project
    val state by Tables.ProjectStates.state
    val setAt by Tables.ProjectStates.setAt
}


class EntityTeam(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityTeam>(Tables.Teams)

    val name by Tables.Teams.name
    val description by Tables.Teams.description
    val color by Tables.Teams.color
    val createdAt by Tables.Teams.createdAt

    val roles by EntityTeamRole referrersOn Tables.TeamRoles.team

    val parentTeams by EntityTeam.via(Tables.TeamsToTeams.child, Tables.TeamsToTeams.parent)
    val childTeams by EntityTeam.via(Tables.TeamsToTeams.parent, Tables.TeamsToTeams.child)
}

class EntityTeamRole(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EntityTeamRole>(Tables.TeamRoles)

    val user by Tables.TeamRoles.user
    val team by Tables.TeamRoles.team
    val isAdmin by Tables.TeamRoles.isAdmin
    val isMember by Tables.TeamRoles.isMember
    val isCreator by Tables.TeamRoles.isCreator
}

class EntityUser(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityUser>(Tables.Users)

    val name by Tables.Users.name
    val middleName by Tables.Users.middleName
    val surname by Tables.Users.surname
    val email by Tables.Users.email
    val phoneNumber by Tables.Users.phoneNumber
    val roomNumber by Tables.Users.roomNumber
    val color by Tables.Users.color
    val createdAt by Tables.Users.createdAt
    val states by EntityUserState referrersOn Tables.UserStates.user
    val teams by EntityTeamRole referrersOn Tables.TeamRoles.user
    val projects by EntityProjectRole referrersOn Tables.ProjectRoles.user
    val tasks by EntityTaskRole referrersOn Tables.TaskRoles.user
}

class EntityUserState(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EntityUserState>(Tables.UserStates)

    val user by Tables.UserStates.user
    val state by Tables.UserStates.state
    val setAt by Tables.UserStates.setAt
}


class EntityTask(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityTask>(Tables.Tasks)

    val name by Tables.Tasks.name
    val description by Tables.Tasks.description
    val project by Tables.Tasks.project
    val createdAt by Tables.Tasks.createdAt
    val completedAt by Tables.Tasks.completedAt
    val doneBefore by Tables.Tasks.doneBefore
    val subTasks by EntitySubTask referrersOn Tables.SubTasks.task
    val states by EntityTaskState referrersOn Tables.TaskStates.task
    val users by EntityTaskRole referrersOn Tables.TaskRoles.task
}

class EntityTaskRole(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EntityTaskRole>(Tables.TaskRoles)

    val user by Tables.TaskRoles.user
    val task by Tables.TaskRoles.task
    val isCreator by Tables.TaskRoles.isCreator
    val isManager by Tables.TaskRoles.isManager
    val isMember by Tables.TaskRoles.isMember

}

class EntitySubTask(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntitySubTask>(Tables.SubTasks)

    val name by Tables.SubTasks.name
    val description by Tables.SubTasks.description
    val createdAt by Tables.SubTasks.createdAt
    val completedAt by Tables.SubTasks.completedAt
    val targetDate by Tables.SubTasks.targetDate

}

class EntityTaskState(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EntityTaskState>(Tables.TaskStates)

    val state by Tables.TaskStates.state
    val setAt by Tables.TaskStates.setAt
}