package di.persistence.repository

import com.akhris.domain.core.exceptions.NotFoundInRepositoryException
import domain.entities.Item
import domain.repository.IItemsRepository

class ItemsTestRepository : IItemsRepository {
    private val repo: MutableMap<String, Item> = mutableMapOf()

    override suspend fun getItemsByType(typeID: String): List<Item> {
        return repo.values.filter { it.type.id == typeID }
    }

    override suspend fun getByID(id: String): Item {
        return repo[id] ?: throw NotFoundInRepositoryException("item with id: $id", this.toString())
    }

    override suspend fun insert(t: Item) {
        repo[t.id] = t
    }

    override suspend fun remove(t: Item) {
        repo.remove(t.id)
    }

    override suspend fun update(t: Item) {
        repo[t.id] = t
    }
}