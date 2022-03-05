package persistence.datasources

import domain.entities.ItemIncome
import domain.entities.Unit

interface IItemsIncomeDao {
    suspend fun getByID(id: String): ItemIncome?
    suspend fun insert(entity: Unit)
    suspend fun update(entity: Unit)
    suspend fun removeById(id: String)
    suspend fun query(offset: Int, limit: Int): List<ItemIncome>
}