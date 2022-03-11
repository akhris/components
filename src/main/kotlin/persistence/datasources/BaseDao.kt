package persistence.datasources

import com.akhris.domain.core.entities.IEntity
import domain.entities.*
import domain.entities.Unit

interface BaseDao<T : IEntity<*>> {
    suspend fun getByID(id: String): T?
    suspend fun getAll(): List<T>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun removeById(id: String)
}

interface BasePagingDao<T : IEntity<*>> {
    suspend fun query(offset: Long, limit: Long): List<T>
    suspend fun getItemsCount(): Long
}


interface IItemsIncomeDao : BaseDao<ItemIncome>, BasePagingDao<ItemIncome>
interface IItemsOutcomeDao : BaseDao<ItemOutcome>, BasePagingDao<ItemOutcome>
interface IUnitsDao : BaseDao<Unit>
interface IParametersDao: BaseDao<Parameter>
interface IItemsDao : BaseDao<Item>, BasePagingDao<Item>