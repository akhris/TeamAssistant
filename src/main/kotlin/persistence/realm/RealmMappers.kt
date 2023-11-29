package persistence.realm

import domain.*
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmSet
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

fun RealmInstant.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(epochSeconds, nanosecondsOfSecond, ZoneOffset.UTC)

fun LocalDateTime.toRealmInstant() = RealmInstant.from(toEpochSecond(ZoneOffset.UTC), nano)


fun RealmUser.toUser(): User =
    User(
        id = _id,
        name = name,
        middleName = middleName,
        surname = surname,
        email = email,
        phoneNumber = phoneNumber,
        roomNumber = roomNumber,
        color = color,
        createdAt = createdAt?.toLocalDateTime()
    )

fun User.toRealmUser(): RealmUser =
    RealmUser().apply {
        _id = this@toRealmUser.id
        name = this@toRealmUser.name
        middleName = this@toRealmUser.middleName
        surname = this@toRealmUser.surname
        email = this@toRealmUser.email
        phoneNumber = this@toRealmUser.phoneNumber
        roomNumber = this@toRealmUser.roomNumber
        color = this@toRealmUser.color
        createdAt = this@toRealmUser.createdAt?.toRealmInstant()
    }

fun RealmTeam.toTeam(): Team =
    Team(
        id = _id,
        name = name,
        creator = creator?.toUser(),
        parentTeamID = parentTeamId?.toHexString(),
        admins = admins.map { it.toUser() },
        members = members.map { it.toUser() },
        createdAt = createdAt?.toLocalDateTime(),
        childTeams = childTeams.map { it.toTeam() }
    )

fun Team.toRealmTeam(): RealmTeam =
    RealmTeam().apply {
        _id = this@toRealmTeam.id
        name = this@toRealmTeam.name
        creator = this@toRealmTeam.creator?.toRealmUser()
        parentTeamId = this@toRealmTeam.parentTeamID?.let { ObjectId(it) }
        admins = this@toRealmTeam.admins.map { it.toRealmUser() }.toRealmSet()
        members = this@toRealmTeam.members.map { it.toRealmUser() }.toRealmSet()
        createdAt = this@toRealmTeam.createdAt?.toRealmInstant()
        childTeams = this@toRealmTeam.childTeams.map { it.toRealmTeam() }.toRealmSet()
    }

fun RealmProject.toProject(): Project =
    Project(
        id = _id,
        name = name,
        description = description,
        color = color,
        creator = creator?.toUser(),
        createdAt = createdAt?.toLocalDateTime(),
        teams = teams.map { it.toTeam() }
    )

fun Project.toRealmProject(): RealmProject =
    RealmProject().apply {
        _id = this@toRealmProject.id
        name = this@toRealmProject.name
        description = this@toRealmProject.description
        color = this@toRealmProject.color
        creator = this@toRealmProject.creator?.toRealmUser()
        createdAt = this@toRealmProject.createdAt?.toRealmInstant()
        teams = this@toRealmProject.teams.map { it.toRealmTeam() }.toRealmSet()
    }


fun Task.toRealmTask(): RealmTask =
    RealmTask().apply {
        _id = this@toRealmTask.id
        name = this@toRealmTask.name
        description = this@toRealmTask.description
        project = this@toRealmTask.project?.toRealmProject()
        creator = this@toRealmTask.creator?.toRealmUser()
        createdAt = this@toRealmTask.createdAt?.toRealmInstant()
        completedAt = this@toRealmTask.completedAt?.toRealmInstant()
        targetDate = this@toRealmTask.targetDate?.toRealmInstant()
        users = this@toRealmTask.users.map { it.toRealmUser() }.toRealmSet()
        subtasks = this@toRealmTask.subtasks.map { it.toRealmSubTask() }.toRealmSet()
    }

fun RealmTask.toTask(): Task =
    Task(
        id = _id,
        name = name,
        description = description,
        project = project?.toProject(),
        creator = creator?.toUser(),
        createdAt = createdAt?.toLocalDateTime(),
        completedAt = completedAt?.toLocalDateTime(),
        targetDate = targetDate?.toLocalDateTime(),
        users = users.map { it.toUser() },
        subtasks = subtasks.map { it.toSubTask() }
    )

fun SubTask.toRealmSubTask(): RealmSubTask =
    RealmSubTask().apply {
        _id = this@toRealmSubTask.id
        name = this@toRealmSubTask.name
        description = this@toRealmSubTask.description
        createdAt = this@toRealmSubTask.createdAt?.toRealmInstant()
        targetDate = this@toRealmSubTask.targetDate?.toRealmInstant()
        completedAt = this@toRealmSubTask.completedAt?.toRealmInstant()
    }

fun RealmSubTask.toSubTask(): SubTask =
    SubTask(
        id = _id,
        name = name,
        description = description,
        createdAt = createdAt?.toLocalDateTime(),
        targetDate = targetDate?.toLocalDateTime(),
        completedAt = completedAt?.toLocalDateTime()
    )