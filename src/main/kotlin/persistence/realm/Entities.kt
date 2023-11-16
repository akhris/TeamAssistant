package persistence.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Task() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    val name: String = ""
    val description: String = ""
    val project: Project
}

class Project() : RealmObject {

}