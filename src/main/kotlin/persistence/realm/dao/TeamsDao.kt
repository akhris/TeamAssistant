package persistence.realm.dao

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import persistence.realm.*

class TeamsDao(private val realm: Realm) : IDao<Team> {

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/read/#find-object-by-primary-key
     */
    override suspend fun getByID(id: String): Team? {
        return realm
            .query<RealmTeam>("_id == $0", id)
            .find()
            .firstOrNull()
            ?.toTeam()
    }

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/delete/#delete-a-single-object
     */
    override suspend fun removeById(id: String) {
        realm.write {
            val teamToDelete = query<RealmTeam>("_id == $0", id).find().firstOrNull()
            teamToDelete?.let { delete(it) }
        }
    }

    override suspend fun query(specs: List<ISpecification>): EntitiesList<Team> {
        return (specs.find { it is Specification.GetAllForUserID } as? Specification.GetAllForUserID)?.let { spec: Specification.GetAllForUserID ->
            EntitiesList.NotGrouped(
                realm
                    .query<RealmTeam>("admins._id == $0 OR members._id == $0", spec.userID)
                    .find()
                    .map { it.toTeam() }
            )
        } ?: EntitiesList.empty()
    }

    override suspend fun getItemsCount(specs: List<ISpecification>): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(entities: List<Team>) {
        entities.forEach {
            update(it)
        }
    }

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/update/#update-a-realm-object
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/update/#upsert-a-realm-object
     */
    override suspend fun update(entity: Team) {
        val mappedUser = entity.toRealmTeam()
        realm.write {
            //make an upsert
            copyToRealm(mappedUser, UpdatePolicy.ALL)
        }
    }

    /**
     * https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/update/#upsert-a-realm-object
     */
    override suspend fun insert(entity: Team) {
        realm.write {
            copyToRealm(entity.toRealmTeam())
        }
    }
}