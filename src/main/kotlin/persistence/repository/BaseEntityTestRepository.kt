package persistence.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.BaseCachedRepository
import com.akhris.domain.core.repository.ISpecification

open class BaseEntityTestRepository<ID : Any, T : IEntity<ID>> : BaseCachedRepository<ID, T>() {

    protected val repo: MutableMap<ID, T> = mutableMapOf()


    override suspend fun fetchItemFromRepo(idToFetch: ID): T? {
        return repo[idToFetch]
    }

    override suspend fun insertInRepo(entity: T) {
        repo[entity.id] = entity
    }

    override suspend fun removeFromRepo(entity: T) {
        repo.remove(entity.id)
    }

    override suspend fun updateInRepo(entity: T) {
        repo[entity.id] = entity
    }

    protected open suspend fun query(specification: Specification): List<T> {
        return when (specification) {
            Specification.QueryAll -> repo.values.toList()
        }
    }

    override suspend fun query(specification: ISpecification): List<T> {
        return if (specification is Specification) {
            query(specification)
        } else listOf()
    }

    override suspend fun remove(specification: ISpecification) {
        TODO("Not yet implemented")
    }
}