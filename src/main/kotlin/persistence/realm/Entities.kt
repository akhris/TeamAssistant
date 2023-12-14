package persistence.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.*
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.util.*

class RealmTask() : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var project: RealmProject? = null
    var creator: RealmUser? = null
    var createdAt: RealmInstant? = null
    var targetDate: RealmInstant? = null
    var completedAt: RealmInstant? = null
    var users: RealmSet<RealmUser> = realmSetOf()
    var subtasks: RealmSet<RealmSubTask> = realmSetOf()
    var attachments: RealmList<RealmAttachment> = realmListOf()
    var isPinned: Boolean = false
    var priority: Int = 1
}

class RealmSubTask() : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var createdAt: RealmInstant? = null
    var targetDate: RealmInstant? = null
    var completedAt: RealmInstant? = null
}

class RealmProject() : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var color: Int? = null
    var creator: RealmUser? = null
    var admins: RealmSet<RealmUser> = realmSetOf()
    var createdAt: RealmInstant? = null
    var teams: RealmSet<RealmTeam> = realmSetOf()
    var isPinned: Boolean = false
    var icon: String = ""
    var tags: RealmSet<String> = realmSetOf()
}

class RealmUser : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var name: String = ""
    var middleName: String = ""
    var surname: String = ""
    var email: String = ""
    var phoneNumber: String = ""
    var roomNumber: String = ""
    var color: Int? = null
    var createdAt: RealmInstant? = null
    var lastOnline: RealmInstant? = null
    var avatar: String = ""
    var isPinned: Boolean = false
}

class RealmTeam : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var color: Int? = null
    var creator: RealmUser? = null
    var members: RealmSet<RealmUser> = realmSetOf()
    var admins: RealmSet<RealmUser> = realmSetOf()
    var childTeams: RealmSet<RealmTeam> = realmSetOf()
    var parentTeamId: ObjectId? = null
    var createdAt: RealmInstant? = null
    var isPinned: Boolean = false
}

class RealmAttachment : EmbeddedRealmObject {
    var type: String = ""
    var name: String = ""
    var description: String = ""
    var dataPrimary: String = ""
    var dataSecondary: String = ""
}
