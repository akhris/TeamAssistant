package persistence.realm.dao

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import persistence.realm.RealmUser
import persistence.realm.toRealmUser
import persistence.realm.toUser

class UsersDao(private val realm: Realm) : IDao<User> {

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/read/#find-object-by-primary-key
     */
    override suspend fun getByID(id: String): User? {
        return realm
            .query<RealmUser>("_id == $0", id)
            .find()
            .firstOrNull()
            ?.toUser()
    }

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/delete/#delete-a-single-object
     */
    override suspend fun removeById(id: String) {
        realm.write {
            val userToDelete = query<RealmUser>("_id == $0", id).find().firstOrNull()
            userToDelete?.let { delete(it) }
        }
    }

    override suspend fun query(specs: List<ISpecification>): EntitiesList<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsCount(specs: List<ISpecification>): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(entities: List<User>) {
        entities.forEach {
            update(it)
        }
    }

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/update/#update-a-realm-object
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/update/#upsert-a-realm-object
     */
    override suspend fun update(entity: User) {
        val mappedUser = entity.toRealmUser()
        realm.write {
            //make an upsert
            copyToRealm(mappedUser, UpdatePolicy.ALL)
        }
    }

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/update/#upsert-a-realm-object
     */
    override suspend fun insert(entity: User) {
        realm.write {
            copyToRealm(entity.toRealmUser())
        }
    }
}