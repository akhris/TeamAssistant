package persistence.realm.repository

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import persistence.realm.*
import utils.log

class RealmTasksRepository(private val realm: Realm) : IRepositoryObservable<Task> {
    override fun getByID(id: String): Flow<RepoResult<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun remove(specifications: List<ISpecification>) {
        TODO("Not yet implemented")
    }

    override fun query(specifications: List<ISpecification>): Flow<EntitiesList<Task>> {
        return (specifications.find { it is Specification.GetAllForUserID } as? Specification.GetAllForUserID)?.let { spec: Specification.GetAllForUserID ->
            //show tasks:
            //1. all team tasks for team admin
            //2. all user-created tasks
            //3. all tasks with user in list
            realm
                .query<RealmTask>(
                    "creator._id == $0 OR users._id == $0 OR project.creator._id == $0 OR project.admins._id == $0",
                    spec.userID
                )
                .find()
                .asFlow()
                .map {
                    EntitiesList.NotGrouped(
                        it
                            .list
                            .map { it.toTask() }
                    )
                }
                .distinctUntilChanged()


        } ?: flowOf(EntitiesList.empty())
    }

    override suspend fun insert(entity: Task) {
        realm.write {
            try {
                copyToRealm(entity.toRealmTask(), updatePolicy = UpdatePolicy.ALL)
            } catch (e: Throwable) {
                log(e.localizedMessage)
            }
        }
    }

    override suspend fun update(entities: List<Task>) {
        entities.forEach {
            update(it)
        }
    }

    override suspend fun update(entity: Task) {
        val mappedTask = entity.toRealmTask()
        realm.write {
            //make an upsert
            copyToRealm(mappedTask, UpdatePolicy.ALL)
        }
    }

    override suspend fun remove(entity: Task) {
        realm.write {
            val taskToDelete = query<RealmTask>("_id == $0", entity.id).find().firstOrNull()
            taskToDelete?.let { delete(it) }
        }
    }

}