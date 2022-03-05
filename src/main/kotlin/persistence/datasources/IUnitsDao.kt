package persistence.datasources

interface IUnitsDao {
    suspend fun getByID(id: String): domain.entities.Unit?
    suspend fun getAll(): List<domain.entities.Unit>
    suspend fun insert(entity: domain.entities.Unit)
    suspend fun update(entity: domain.entities.Unit)
    suspend fun removeById(id: String)
//    suspend fun getItemsCount(): Long
}