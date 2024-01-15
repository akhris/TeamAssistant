package persistence.realm.repository

import domain.*
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.DeletedObject
import io.realm.kotlin.notifications.InitialObject
import io.realm.kotlin.notifications.PendingObject
import io.realm.kotlin.notifications.UpdatedObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import persistence.realm.*
import utils.log

class RealmSettingsRepository(private val realm: Realm) : ISettingsRepository {
    override fun getByID(id: String): Flow<RepoResult<Setting>> {
        return realm
            .query<RealmSetting>("_id == $0", id)
            .first()
            .asFlow()
            .map { setting ->
                when (setting) {
                    is DeletedObject -> RepoResult.ItemRemoved(setting.obj?.toSetting())
                    is InitialObject -> RepoResult.InitialItem(setting.obj.toSetting())
                    is UpdatedObject -> RepoResult.ItemUpdated(setting.obj.toSetting())
                    is PendingObject -> RepoResult.PendindObject()
                }
            }
            .distinctUntilChanged()
    }


    override fun query(specifications: List<ISpecification>): Flow<List<Setting>> {
        var query = realm.query<RealmSetting>()

        specifications
            .filterIsInstance(Specification.QueryAll::class.java)
            .forEach { spec ->
//                query = query
                //query is already contains all items - do nothing
            }
        return query
            .find()
            .asFlow()
            .map { realmSettings ->
                realmSettings.list.map { it.toSetting() }
            }
            .distinctUntilChanged()
    }

    override suspend fun insert(entity: Setting) {
        realm.write {
            try {
                copyToRealm(entity.toRealmSetting(), updatePolicy = UpdatePolicy.ALL)
            } catch (e: Throwable) {
                log(e.localizedMessage)
            }
        }
    }


    override suspend fun update(entity: Setting) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(entity: Setting) {
        TODO("Not yet implemented")
    }
}