package domain

import domain.RepoResult.*
import domain.settings.Setting
import domain.valueobjects.SliceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow


/**
 * Base Repository interface, kind of CRUD.
 * Supposing that all entities have
 */
interface IRepository<ENTITY : IEntity> {
    suspend fun getByID(id: String): ENTITY
    suspend fun remove(t: ENTITY)
    suspend fun update(t: ENTITY)
    suspend fun update(t: List<ENTITY>)
    suspend fun insert(t: ENTITY)
    suspend fun remove(specifications: List<ISpecification>)
    suspend fun query(specifications: List<ISpecification>): EntitiesList<ENTITY>
    suspend fun getItemsCount(specifications: List<ISpecification>): Long
    suspend fun getSlice(columnName: String): List<SliceResult>
}

interface IRepositoryObservable<ENTITY : IEntity> {
    fun getByID(id: String): Flow<RepoResult<ENTITY>>
    suspend fun remove(entity: ENTITY)
    suspend fun update(entity: ENTITY)
    suspend fun update(entities: List<ENTITY>)
    suspend fun insert(entity: ENTITY)
    suspend fun remove(specifications: List<ISpecification>)
    fun query(specifications: List<ISpecification>): Flow<EntitiesList<ENTITY>>
    fun getFilterSpecs(): Flow<List<FilterSpec>>?
}

interface ISettingsRepository {
    suspend fun getStringSetting(id: String): Setting.StringSetting?
    suspend fun getBooleanSetting(id: String): Setting.BooleanSetting?
    suspend fun getPathSetting(id: String): Setting.PathSetting?

    fun query(specifications: List<ISpecification>): Flow<List<Setting>>

    suspend fun update(setting: Setting)

    suspend fun insert(setting: Setting)
    suspend fun insert(settings: List<Setting>)

    suspend fun remove(entity: Setting)

}


/**
 * Marker interface for querying list of data from repository
 * https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006
 */
interface ISpecification

/**
 * Result sealed class that is used in [IRepositoryCallback].
 * It returns whether item was updated ([ItemUpdated]), inserted [ItemInserted] or removed [ItemRemoved] from the repo.
 */
sealed class RepoResult<T>(val item: T?) {
    class InitialItem<T>(item: T?) : RepoResult<T>(item)
    class ItemUpdated<T>(item: T?) : RepoResult<T>(item)
    class ItemRemoved<T>(item: T?) : RepoResult<T>(item)
    class ItemInserted<T>(item: T?) : RepoResult<T>(item)
    class PendindObject<T> : RepoResult<T>(null)
}

/**
 * Repository callbacks interface for getting repo updates as [SharedFlow]
 */
interface IRepositoryCallback<T> {
    val updates: SharedFlow<RepoResult<T>>
}