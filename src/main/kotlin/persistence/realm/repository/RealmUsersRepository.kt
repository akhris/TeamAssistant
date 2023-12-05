package persistence.realm.repository

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.DeletedObject
import io.realm.kotlin.notifications.InitialObject
import io.realm.kotlin.notifications.PendingObject
import io.realm.kotlin.notifications.UpdatedObject
import kotlinx.coroutines.flow.*
import persistence.realm.*
import utils.log

class RealmUsersRepository(private val realm: Realm) : IRepositoryObservable<User> {
    override fun getByID(id: String): Flow<RepoResult<User>> {
        return realm
            .query<RealmUser>("_id == $0", id)
            .first()
            .asFlow()
            .map { result ->
                when (result) {
                    is DeletedObject -> RepoResult.ItemRemoved(result.obj?.toUser())
                    is InitialObject -> RepoResult.InitialItem(result.obj.toUser())
                    is UpdatedObject -> RepoResult.ItemUpdated(result.obj.toUser())
                    is PendingObject -> RepoResult.PendindObject()
                }
            }
            .distinctUntilChanged()
    }

    override suspend fun remove(specifications: List<ISpecification>) {
        TODO("Not yet implemented")
    }

    override fun query(specifications: List<ISpecification>): Flow<EntitiesList<User>> {
        return (specifications
            .find { it is Specification.QueryAll } as? Specification.QueryAll)
            ?.let { s ->
                realm
                    .query<RealmUser>()
                    .find()
                    .asFlow()
                    .map {
                        EntitiesList.NotGrouped(
                            it
                                .list
                                .map { it.toUser() }
                        )
                    }
                    .distinctUntilChanged()
            } ?: flowOf(EntitiesList.empty())
    }

    override fun getFilterSpecs(): Flow<List<FilterSpec>>? = null

    override suspend fun insert(entity: User) {
        realm.write {
            try {
                copyToRealm(entity.toRealmUser(), updatePolicy = UpdatePolicy.ALL)
            } catch (e: Throwable) {
                log(e.localizedMessage)
            }
        }
    }

    override suspend fun update(entities: List<User>) {
        entities.forEach {
            update(it)
        }
    }

    override suspend fun update(entity: User) {
        val mappedUser = entity.toRealmUser()
        realm.write {
            //make an upsert
            copyToRealm(mappedUser, UpdatePolicy.ALL)
        }
    }

    override suspend fun remove(entity: User) {
        realm.write {
            val userToDelete = query<RealmUser>("_id == $0", entity.id).find().firstOrNull()
            userToDelete?.let { delete(it) }
        }
    }
}