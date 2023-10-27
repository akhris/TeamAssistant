package persistence.exposed.dto

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Tables {

    /**
     * Users table.
     */
    object Users : UUIDTable() {
        val name = text(name = "name")
        val middleName = text(name = "middleName")
        val surname = text(name = "surname")
        val email = text(name = "email")
        val phoneNumber = text(name = "phoneNumber")
        val roomNumber = text(name = "roomNumber")
        val color = integer(name = "color").nullable()
        val createdAt = datetime(name = "createdAt").nullable()
    }

    object UserStates : LongIdTable() {
        val user = reference(name = "user", foreign = Users)
        val state = text(name = "state")
        val setAt = datetime(name = "set_at")
    }

    object Teams : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
        val color = integer(name = "color").nullable()
        val createdAt = datetime(name = "createdAt").nullable()
    }

    object TeamsToTeams : Table() {
        val parent = reference("parent_team", Teams)
        val child = reference("child_team", Teams)
    }

    object TeamRoles : LongIdTable() {
        val user = reference(name = "user", foreign = Users)
        val team = reference(name = "team", foreign = Teams)
        val isAdmin = bool(name = "isAdmin")
        val isMember = bool(name = "isMember")
        val isCreator = bool(name = "isCreator")
    }

    object Projects : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
        val color = integer(name = "color").nullable()
        val createdAt = datetime(name = "createdAt").nullable()
        val createdBy = reference(name = "createdBy", foreign = Users)
    }

    object ProjectRoles : LongIdTable() {
        val user = reference(name = "user", foreign = Users)
        val project = reference(name = "project", foreign = Projects)
        val isCreator = bool(name = "isCreator")
        val isManager = bool(name = "isManager")
    }

    object ProjectStates : LongIdTable() {
        val project = reference(name = "project", foreign = Projects)
        val state = text(name = "state")
        val setAt = datetime(name = "set_at")
    }


    object Tasks : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
        val project = reference(name = "project", foreign = Projects).nullable()
        val createdAt = datetime(name = "createdAt").nullable()
        val completedAt = datetime(name = "completedAt").nullable()
        val doneBefore = datetime(name = "doneBefore").nullable()
    }

    object TaskRoles : LongIdTable() {
        val user = reference(name = "user", foreign = Users)
        val task = reference(name = "task", foreign = Tasks)
        val isCreator = bool(name = "isCreator")
        val isManager = bool(name = "isManager")
        val isMember = bool(name = "isMember")
    }

    object TaskStates : LongIdTable() {
        val task = reference(name = "task", foreign = Tasks)
        val state = text(name = "state")
        val setAt = datetime(name = "set_at")
    }

    object SubTasks : UUIDTable() {
        val task = reference(name = "task", foreign = Tasks)
        val name = text(name = "name")
        val description = text(name = "description")
        val createdAt = datetime(name = "createdAt").nullable()
        val completedAt = datetime(name = "completedAt").nullable()
        val targetDate = datetime(name = "targetDate").nullable()
    }

}
