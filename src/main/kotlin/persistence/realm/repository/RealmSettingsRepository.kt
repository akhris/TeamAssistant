package persistence.realm.repository

import domain.ISettingsRepository
import domain.ISpecification
import domain.Specification
import domain.settings.Setting
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import persistence.realm.RealmSetting
import persistence.realm.toRealmSetting
import persistence.realm.toSetting
import utils.log

class RealmSettingsRepository(private val realm: Realm) : ISettingsRepository {


    override suspend fun getStringSetting(id: String): Setting.StringSetting? {
        return realm
            .query<RealmSetting>("_id == $0", id)
            .first()
            .find()?.toSetting() as? Setting.StringSetting
    }

    override suspend fun getBooleanSetting(id: String): Setting.BooleanSetting? {
        return realm
            .query<RealmSetting>("_id == $0", id)
            .first()
            .find()?.toSetting() as? Setting.BooleanSetting
    }

    override suspend fun getPathSetting(id: String): Setting.PathSetting? {
        return realm
            .query<RealmSetting>("_id == $0", id)
            .first()
            .find()?.toSetting() as? Setting.PathSetting
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

    override suspend fun insert(setting: Setting) {
        realm.write {
            try {
                copyToRealm(setting.toRealmSetting(), updatePolicy = UpdatePolicy.ALL)
            } catch (e: Throwable) {
                log(e.localizedMessage)
            }
        }
    }

    override suspend fun insert(settings: List<Setting>) {
        settings.forEach {
            insert(it)
        }
    }

    override suspend fun update(setting: Setting) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(entity: Setting) {
        TODO("Not yet implemented")
    }
}