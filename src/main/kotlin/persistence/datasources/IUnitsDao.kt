package persistence.datasources

import persistence.dto.exposed.EntityUnit

interface IUnitsDao {
    suspend fun getByID(id: String): EntityUnit?
    suspend fun getAll(): List<EntityUnit>
    suspend fun insert(entity: EntityUnit)
    suspend fun update(entity: EntityUnit)
    suspend fun removeById(id: String)
//    suspend fun getItemsCount(): Long
}