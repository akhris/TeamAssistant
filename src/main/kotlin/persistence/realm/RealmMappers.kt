package persistence.realm

import domain.*
import domain.valueobjects.Attachment
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
        createdAt = createdAt?.toLocalDateTime(),
        lastOnline = lastOnline?.toLocalDateTime(),
        avatar = avatar,
        isPinned = isPinned
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
        lastOnline = this@toRealmUser.lastOnline?.toRealmInstant()
        avatar = this@toRealmUser.avatar
        isPinned = this@toRealmUser.isPinned
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
        childTeams = childTeams.map { it.toTeam() },
        isPinned = isPinned
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
        isPinned = this@toRealmTeam.isPinned
    }

fun RealmProject.toProject(): Project =
    Project(
        id = _id,
        name = name,
        description = description,
        color = color,
        creator = creator?.toUser(),
        createdAt = createdAt?.toLocalDateTime(),
        teams = teams.map { it.toTeam() },
        isPinned = isPinned
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
        isPinned = this@toRealmProject.isPinned
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
        attachments = this@toRealmTask.attachments.map { it.toRealmAttachment() }.toRealmList()
        isPinned = this@toRealmTask.isPinned
        priority = this@toRealmTask.priority
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
        subtasks = subtasks.map { it.toSubTask() },
        attachments = attachments.map { it.toAttachment() },
        isPinned = isPinned,
        priority = priority
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

fun RealmAttachment.toAttachment(): Attachment {
    return when (type) {
        Attachment.File::class.simpleName -> Attachment.File(path = dataPrimary, name = name, description = description)
        Attachment.Folder::class.simpleName -> Attachment.Folder(path = dataPrimary, name = name, description = description)
        Attachment.InternetLink::class.simpleName -> Attachment.InternetLink(link = dataPrimary, name = name, description = description)
        Attachment.Email::class.simpleName -> Attachment.Email(email = dataPrimary, name = name, description = description)
        else -> throw IllegalStateException("unknown attachment type: $type")
    }
}

fun Attachment.toRealmAttachment(): RealmAttachment {
    return RealmAttachment().apply {
        when (val a = this@toRealmAttachment) {
            is Attachment.Email -> {
                type = Attachment.Email::class.simpleName
                    ?: throw IllegalStateException("cannot convert $this to RealmAttachment")
                dataPrimary = a.email
                name = a.name
                description = a.description
            }

            is Attachment.File -> {
                type = Attachment.File::class.simpleName
                    ?: throw IllegalStateException("cannot convert $this to RealmAttachment")
                dataPrimary = a.path
                name = a.name
                description = a.description
            }

            is Attachment.Folder -> {
                Attachment.Folder::class.simpleName
                dataPrimary = a.path
                name = a.name
                description = a.description
            }

            is Attachment.InternetLink -> {
                Attachment.InternetLink::class.simpleName
                dataPrimary = a.link
                name = a.name
                description = a.description
            }
        }
    }
}