package persistence.realm.repository

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.*
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

    override fun getByIDBlocking(id: String): RepoResult<Project> {
        val realmItem = realm
            .query<RealmProject>("_id == $0", id)
            .first()
            .find()
        return realmItem?.let {
            RepoResult.InitialItem(it.toProject())
        } ?: RepoResult.PendindObject()
    }

    override suspend fun remove(specifications: List<ISpecification>) {
        TODO("Not yet implemented")
    }

    override fun query(specifications: List<ISpecification>): Flow<EntitiesList<Project>> {
        var query = realm.query<RealmProject>()

        specifications
            .filterIsInstance(Specification.GetAllForUserID::class.java)
            .forEach { spec ->
                query = query
                    .query("admins._id == $0 OR creator._id == $0", spec.userID)
            }
        return query
            .find()
            .asFlow()
            .map { realmProjects ->
                EntitiesList.NotGrouped(
                    realmProjects.list.map { it.toProject() }
                )
            }
            .distinctUntilChanged()

    }

    override suspend fun queryBlocking(specifications: List<ISpecification>): EntitiesList<Project> {
        var query = realm.query<RealmProject>()

        specifications
            .filterIsInstance(Specification.GetAllForUserID::class.java)
            .forEach { spec ->
                query = query
                    .query("admins._id == $0 OR creator._id == $0", spec.userID)
            }
        val a = query
            .find().map {
                it.toProject()
            }
        return EntitiesList.NotGrouped(a)
    }

    override fun getFilterSpecs(): Flow<List<FilterSpec>> {
        return realm
            .query<RealmProject>()
            .find()
            .asFlow()
            .map { projectResult ->
                val projects = when (projectResult) {
                    is InitialResults -> projectResult.list
                    is UpdatedResults -> projectResult.list
                }
                projects
                    .flatMap { task ->
                        listOf(
                            "teams" to task.teams?.map { it.toTeam() }
                        )
                    }
                    .groupBy({ it.first }, { it.second })   //todo flatmap teams
                    .map { FilterSpec.Values(columnName = it.key, filteredValues = it.value) }

            }
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