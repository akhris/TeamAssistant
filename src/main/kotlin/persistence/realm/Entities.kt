package persistence.realm

import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.*
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.*

class RealmTask: RealmObject {
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
    var parentTask: RealmTask? = null
    var subchecks: RealmSet<RealmSubCheck> = realmSetOf()
    var attachments: RealmList<RealmAttachment> = realmListOf()
    var isPinned: Boolean = false
    var priority: Int = 1
    var messages: RealmList<RealmTaskMessage> = realmListOf()
    var usersLastOnline: RealmDictionary<RealmInstant> = realmDictionaryOf()
}

class RealmSubCheck : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var createdAt: RealmInstant? = null
    var targetDate: RealmInstant? = null
    var completedAt: RealmInstant? = null
    var completedBy: RealmUser? = null
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
    var isDBCreator: Boolean = false
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

class RealmTaskMessage : EmbeddedRealmObject {
    var text: String = ""
    var user: RealmUser? = null
    var createdAt: RealmInstant? = null
    var attachments: RealmList<RealmAttachment> = realmListOf()
}

// Settings that are stored in database and common for all users of that database file
class RealmSetting : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var value: String = ""
//    var type: String = ""
}

const val REALMSETTING_TYPE_STRING = "realm.setting.type.string"
const val REALMSETTING_TYPE_BOOLEAN = "realm.setting.type.boolean"
const val REALMSETTING_TYPE_PATH = "realm.setting.type.path"


class RealmDBPolicy : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var name: String = ""
    var value: String = ""
    var defaultValue: String = ""
    var possibleValues: RealmSet<String> = realmSetOf()
}