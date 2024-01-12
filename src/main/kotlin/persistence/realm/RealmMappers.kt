package persistence.realm

import domain.*
import domain.valueobjects.Attachment
import domain.valueobjects.TaskMessage
import io.realm.kotlin.ext.toRealmDictionary
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.types.RealmInstant
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset

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
        isPinned = isPinned,
        isDBCreator = isDBCreator
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
        isDBCreator = this@toRealmUser.isDBCreator
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
        isPinned = isPinned,
        icon = icon,
        tags = tags.toSet()
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
        icon = this@toRealmProject.icon
        tags = this@toRealmProject.tags.toRealmSet()
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
        messages = this@toRealmTask.messages.map { it.toRealmTaskMessage() }.toRealmList()
        usersLastOnline =
            this@toRealmTask.usersLastOnline.map { it.key to it.value.toRealmInstant() }.toRealmDictionary()
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
        priority = priority,
        messages = messages.map { it.toTaskMessage() },
        usersLastOnline = usersLastOnline.map { it.key to it.value.toLocalDateTime() }.toMap()
    )

fun SubTask.toRealmSubTask(): RealmSubTask =
    RealmSubTask().apply {
        _id = this@toRealmSubTask.id
        name = this@toRealmSubTask.name
        description = this@toRealmSubTask.description
        createdAt = this@toRealmSubTask.createdAt?.toRealmInstant()
        targetDate = this@toRealmSubTask.targetDate?.toRealmInstant()
        completedAt = this@toRealmSubTask.completedAt?.toRealmInstant()
        completedBy = this@toRealmSubTask.completedBy?.toRealmUser()
    }

fun RealmSubTask.toSubTask(): SubTask =
    SubTask(
        id = _id,
        name = name,
        description = description,
        createdAt = createdAt?.toLocalDateTime(),
        targetDate = targetDate?.toLocalDateTime(),
        completedAt = completedAt?.toLocalDateTime(),
        completedBy = completedBy?.toUser()
    )

fun RealmAttachment.toAttachment(): Attachment {
    return when (type) {
        Attachment.File::class.simpleName -> Attachment.File(path = dataPrimary, name = name, description = description)
        Attachment.Folder::class.simpleName -> Attachment.Folder(
            path = dataPrimary,
            name = name,
            description = description
        )

        Attachment.InternetLink::class.simpleName -> Attachment.InternetLink(
            link = dataPrimary,
            name = name,
            description = description
        )

        Attachment.Email::class.simpleName -> Attachment.Email(
            email = dataPrimary,
            name = name,
            description = description
        )

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
                type = Attachment.Folder::class.simpleName
                    ?: throw IllegalStateException("cannot convert $this to RealmAttachment")
                dataPrimary = a.path
                name = a.name
                description = a.description
            }

            is Attachment.InternetLink -> {
                type = Attachment.InternetLink::class.simpleName
                    ?: throw IllegalStateException("cannot convert $this to RealmAttachment")
                dataPrimary = a.link
                name = a.name
                description = a.description
            }
        }
    }
}

fun RealmTaskMessage.toTaskMessage(): TaskMessage {
    return TaskMessage(
        text = text,
        createdAt = createdAt?.toLocalDateTime(),
        user = user?.toUser(),
        attachments = attachments.map { it.toAttachment() }
    )
}

fun TaskMessage.toRealmTaskMessage(): RealmTaskMessage {
    return RealmTaskMessage().apply {
        text = this@toRealmTaskMessage.text
        user = this@toRealmTaskMessage.user?.toRealmUser()
        createdAt = this@toRealmTaskMessage.createdAt?.toRealmInstant()
        attachments = this@toRealmTaskMessage.attachments.map { it.toRealmAttachment() }.toRealmList()
    }
}

fun RealmSetting.toSetting(): Setting {
    return when (type) {
        Setting.TYPE_BOOLEAN -> Setting.BooleanSetting(
            id = this._id,
            name = this.name,
            description = this.description,
            value = this.value.toBoolean()
        )
        Setting.TYPE_STRING -> Setting.StringSetting(
            id = this._id,
            name = this.name,
            description = this.description,
            value = this.value
        )
        else -> throw IllegalArgumentException("unknown setting type: $type")
    }
}

fun Setting.toRealmSetting(): RealmSetting {
    return RealmSetting().apply {
        _id = this@toRealmSetting.id
        name = this@toRealmSetting.name
        description = this@toRealmSetting.description
        type = when (this@toRealmSetting) {
            is Setting.BooleanSetting -> Setting.TYPE_BOOLEAN
            is Setting.StringSetting -> Setting.TYPE_STRING
        }
        value = when (this@toRealmSetting) {
            is Setting.BooleanSetting -> this@toRealmSetting.value.toString()
            is Setting.StringSetting -> this@toRealmSetting.value
        }
    }
}