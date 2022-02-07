package persistence.repository

import com.akhris.domain.core.repository.BaseCachedRepository
import domain.entities.ObjectType
import domain.repository.ITypesRepository

class ObjectTypesTestRepository : BaseCachedRepository<String, ObjectType>(), ITypesRepository {
    private val repo: MutableMap<String, ObjectType> = mutableMapOf()

    override suspend fun getAllItemsTypes(): List<ObjectType> {
        return repo.values.toList()
    }

    override suspend fun fetchItemFromRepo(idToFetch: String): ObjectType? {
        return repo[idToFetch]
    }

    override suspend fun insertInRepo(entity: ObjectType) {
        repo[entity.id] = entity
    }

    override suspend fun removeFromRepo(entity: ObjectType) {
        repo.remove(entity.id)
    }

    override suspend fun updateInRepo(entity: ObjectType) {
        repo[entity.id] = entity
    }

}