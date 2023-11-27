package persistence.repositories

import domain.*
import domain.valueobjects.SliceResult
import kotlinx.coroutines.flow.SharedFlow

class Repository<ENTITY : IEntity>(private val dao: IDao<ENTITY>) : IRepository<ENTITY>, IRepositoryCallback<ENTITY> {

    private val repoCallbacks = RepositoryCallbacks<ENTITY>()
    override val updates: SharedFlow<RepoResult<ENTITY>> = repoCallbacks.updates

    override suspend fun getByID(id: String): ENTITY {
        return dao.getByID(id) ?: throw NotFoundInRepositoryException(
            what = "entity with id: $id",
            repository = this.toString()
        )
    }

    override suspend fun remove(specifications: List<ISpecification>) {
        TODO("Not yet implemented")
    }

    override suspend fun query(specifications: List<ISpecification>): EntitiesList<ENTITY> {
        return dao.query(specifications)
    }

    override suspend fun getItemsCount(specifications: List<ISpecification>): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getSlice(columnName: String): List<SliceResult> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(t: ENTITY) {
        dao.insert(t)
        repoCallbacks.onItemInserted(t)
    }

    override suspend fun update(t: List<ENTITY>) {
        dao.update(t)
    }

    override suspend fun update(t: ENTITY) {
        dao.update(t)
        repoCallbacks.onItemUpdated(t)
    }

    override suspend fun remove(t: ENTITY) {
        dao.removeById(t.id)
        repoCallbacks.onItemRemoved(t)
    }
}