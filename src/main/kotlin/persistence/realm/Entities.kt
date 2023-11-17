package persistence.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.*

class RealmTask() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var description: String = ""
    var realmProject: RealmProject? = null
    var creator: RealmUser? = null
    var createdAt: RealmInstant? = null
    var realmUsers: RealmList<RealmUser> = realmListOf()
}

class RealmProject(

) : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var description: String = ""
    var color: Int? = null
    var creator: RealmUser? = null
    var createdAt: RealmInstant? = null
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

}

