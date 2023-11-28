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

class RealmProjectsRepository(private val realm: Realm) : IRepositoryObservable<Project> {
    override fun getByID(id: String): Flow<RepoResult<Project>> {
        return realm
            .query<RealmProject>("_id == $0", id)
            .first()
            .asFlow()
            .mapNotNull { result ->
                result.obj?.let { resObj ->
                    when (result) {
                        is DeletedObject -> RepoResult.ItemRemoved(resObj.toProject())
                        is InitialObject -> RepoResult.InitialItem(resObj.toProject())
                        is UpdatedObject -> RepoResult.ItemUpdated(resObj.toProject())
                        is PendingObject -> null
                    }
                }
            }
            .distinctUntilChanged()
    }

    override suspend fun remove(specifications: List<ISpecification>) {
        TODO("Not yet implemented")
    }

    override fun query(specifications: List<ISpecification>): Flow<EntitiesList<Project>> {
        return (specifications.find { it is Specification.GetAllForUserID } as? Specification.GetAllForUserID)?.let { spec: Specification.GetAllForUserID ->
            realm
                .query<RealmProject>()
                .find()
                .asFlow()
                .map { projList ->
                    //filter projects available to user with id specified in spec
                    projList
                        .list
                        .filter { proj ->
                            (proj.creator?._id == spec.userID) or (
                                    proj
                                        .teams
                                        .flatMap { it.admins + it.members + listOfNotNull(it.creator) }
                                        .map { it._id }
                                        .contains(spec.userID)
                                    )
                        }
                }
                .map { realmProjects ->
                    EntitiesList.NotGrouped(
                        realmProjects.map { it.toProject() }
                    )
                }
                .distinctUntilChanged()


        } ?: flowOf(EntitiesList.empty())
    }

    override suspend fun insert(entity: Project) {
        realm.write {
            try {
                copyToRealm(entity.toRealmProject(), updatePolicy = UpdatePolicy.ALL)
            } catch (e: Throwable) {
                log(e.localizedMessage)
            }
        }
    }

    override suspend fun update(entities: List<Project>) {
        entities.forEach {
            update(it)
        }
    }

    override suspend fun update(entity: Project) {
        val mappedProject = entity.toRealmProject()
        realm.write {
            //make an upsert
            copyToRealm(mappedProject, UpdatePolicy.ALL)
        }
    }

    override suspend fun remove(entity: Project) {
        realm.write {
            val projectToDelete = query<RealmProject>("_id == $0", entity.id).find().firstOrNull()
            projectToDelete?.let { delete(it) }
        }
    }
}