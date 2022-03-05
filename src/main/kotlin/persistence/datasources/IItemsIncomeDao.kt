package persistence.datasources

import domain.entities.ItemIncome

interface IItemsIncomeDao {
    suspend fun getByID(id: String): ItemIncome?
    suspend fun getAll(): List<ItemIncome>
    suspend fun insert(entity: ItemIncome)
    suspend fun update(entity: ItemIncome)
    suspend fun removeById(id: String)
    suspend fun query(offset: Int, limit: Int): List<ItemIncome>
    suspend fun getItemsCount(): Long
}