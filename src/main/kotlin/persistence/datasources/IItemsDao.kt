package persistence.datasources

import domain.entities.Item

interface IItemsDao {
    suspend fun getByID(id: String): Item?
    suspend fun getAll(): List<Item>
    suspend fun insert(entity: Item)
    suspend fun update(entity: Item)
    suspend fun removeById(id: String)
    suspend fun query(offset: Int, limit: Int): List<Item>
    suspend fun getItemsCount(): Long
}