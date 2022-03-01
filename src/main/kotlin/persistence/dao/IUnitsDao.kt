package persistence.dao

import domain.entities.Unit

interface IUnitsDao {
    suspend fun getByID(id: String): Unit?
    suspend fun getAll(): List<Unit>
    suspend fun insert(entity: Unit)
    suspend fun update(entity: Unit)
    suspend fun removeById(id: String)
    suspend fun getItemsCount(): Long
}