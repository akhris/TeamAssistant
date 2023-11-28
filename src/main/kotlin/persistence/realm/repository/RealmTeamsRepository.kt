package persistence.realm.repository

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.*
import kotlinx.coroutines.flow.*
import persistence.realm.RealmTeam
import persistence.realm.toRealmTeam
import persistence.realm.toTeam
import utils.log

class RealmTeamsRepository(private val realm: Realm) : IRepositoryObservable<Team> {
    override fun getByID(id: String): Flow<RepoResult<Team>> {
        return realm
            .query<RealmTeam>("_id == $0", id)
            .first()
            .asFlow()
            .mapNotNull { result ->
                result.obj?.let { resObj ->
                    when (result) {
                        is DeletedObject -> RepoResult.ItemRemoved(resObj.toTeam())
                        is InitialObject -> RepoResult.InitialItem(resObj.toTeam())
                        is UpdatedObject -> RepoResult.ItemUpdated(resObj.toTeam())
                        is PendingObject -> null
                    }
                }
            }
            .distinctUntilChanged()
    }

    override suspend fun remove(specifications: List<ISpecification>) {
        TODO("Not yet implemented")
    }

    override fun query(specifications: List<ISpecification>): Flow<EntitiesList<Team>> {
        return (specifications.find { it is Specification.GetAllForUserID } as? Specification.GetAllForUserID)?.let { spec: Specification.GetAllForUserID ->
            realm
                .query<RealmTeam>("admins._id == $0 OR members._id == $0 OR creator._id == $0", spec.userID)
                .find()
                .asFlow()
                .map {
                    EntitiesList.NotGrouped(
                        it
                            .list
                            .map { it.toTeam() }
                    )
                }
                .distinctUntilChanged()


        } ?: flowOf(EntitiesList.empty())
    }

    override suspend fun insert(entity: Team) {
        realm.write {
            try {
                copyToRealm(entity.toRealmTeam(), updatePolicy = UpdatePolicy.ALL)
            } catch (e: Throwable) {
                log(e.localizedMessage)
            }
        }
    }

    override suspend fun update(entities: List<Team>) {
        entities.forEach {
            update(it)
        }
    }

    override suspend fun update(entity: Team) {
        val mappedTeam = entity.toRealmTeam()
        realm.write {
            //make an upsert
            copyToRealm(mappedTeam, UpdatePolicy.ALL)
        }
    }

    override suspend fun remove(entity: Team) {
        realm.write {
            val teamToDelete = query<RealmTeam>("_id == $0", entity.id).find().firstOrNull()
            teamToDelete?.let { delete(it) }
        }
    }
}