package persistence.repository

import com.akhris.domain.core.repository.BaseCachedRepository
import domain.entities.Item
import domain.repository.IItemsRepository

class ItemsTestRepository : BaseCachedRepository<String, Item>(), IItemsRepository {
    private val repo: MutableMap<String, Item> = mutableMapOf()

    override suspend fun getItemsByType(typeID: String): List<Item> {
        return repo.values.filter { it.type.id == typeID }
    }

    override suspend fun fetchItemFromRepo(idToFetch: String): Item? {
        return repo[idToFetch]
    }

    override suspend fun insertInRepo(entity: Item) {
        repo[entity.id] = entity
    }

    override suspend fun removeFromRepo(entity: Item) {
        repo.remove(entity.id)
    }

    override suspend fun updateInRepo(entity: Item) {
        repo[entity.id] = entity
    }
}