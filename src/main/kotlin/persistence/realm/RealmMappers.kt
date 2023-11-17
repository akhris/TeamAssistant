package persistence.realm

import domain.User
import io.realm.kotlin.types.RealmInstant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun RealmInstant.toLocalDateTime() = LocalDateTime.ofEpochSecond(epochSeconds, nanosecondsOfSecond, ZoneOffset.UTC)

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