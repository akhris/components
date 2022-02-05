package di.persistence.repository

import com.akhris.domain.core.exceptions.NotFoundInRepositoryException
import domain.entities.Item
import domain.entities.ObjectType
import domain.repository.ITypesRepository

class ObjectTypesTestRepository : ITypesRepository {
    private val repo: MutableMap<String, ObjectType> = mutableMapOf()

    override suspend fun getAllItemsTypes(): List<ObjectType> {
        return repo.values.toList()
    }

    override suspend fun getByID(id: String): ObjectType {
        return repo[id] ?: throw NotFoundInRepositoryException("object type with id: $id", this.toString())
    }

    override suspend fun insert(t: ObjectType) {
        repo[t.id] = t
    }

    override suspend fun remove(t: ObjectType) {
        repo.remove(t.id)
    }

    override suspend fun update(t: ObjectType) {
        repo[t.id] = t
    }

}