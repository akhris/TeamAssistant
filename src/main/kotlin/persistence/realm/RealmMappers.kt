package persistence.realm

import domain.Team
import domain.User
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.types.RealmInstant
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset

fun RealmInstant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofEpochSecond(epochSeconds, nanosecondsOfSecond, ZoneOffset.UTC)

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