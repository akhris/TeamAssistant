package domain.application.baseUseCases

import domain.IEntity
import domain.IRepository
import domain.application.IoDispatcher
import domain.application.UseCase
import kotlinx.coroutines.CoroutineDispatcher


/**
 * Base use case to insert [IEntity] to [IRepository].
 * May be overridden for more complex use cases.
 */
open class InsertEntity<ENTITY : IEntity>(
    private val repo: IRepository<ENTITY>,
        @IoDispatcher
    ioDispatcher: CoroutineDispatcher
) : UseCase<ENTITY, InsertEntity.Params>(ioDispatcher) {

    override suspend fun run(params: Params): ENTITY {
        return when (params) {
            is Insert -> (params.entityToInsert as? ENTITY)?.let { insert(params.entityToInsert) }
                ?: throw IllegalArgumentException("Entity to insert is not type of insert use case: $this")
        }
    }

    private suspend fun insert(entity: ENTITY): ENTITY {
        repo.insert(entity)
        return entity
    }



    sealed class Params
    data class Insert(val entityToInsert: Any) : Params()


}