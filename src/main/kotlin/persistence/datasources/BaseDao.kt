package persistence.datasources

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import domain.entities.*
import domain.entities.Unit
import persistence.repository.FilterSpec
import persistence.repository.IGSFPRepository

interface BaseDao<T : IEntity<*>> {
    suspend fun getByID(id: String): T?
    suspend fun getAll(filters: List<FilterSpec>): List<T>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun removeById(id: String)
}

interface BasePagingDao<T : IEntity<*>> {
    suspend fun query(offset: Long, limit: Long): List<T>
    suspend fun getItemsCount(): Long
}

interface IBaseGSFPDao<T : IEntity<*>> : BaseDao<T> {
    suspend fun query(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<IGSFPRepository.Result<T>>

    suspend fun getItemsCount(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Long
}


interface IItemsIncomeDao : BaseDao<ItemIncome>, BasePagingDao<ItemIncome>
interface IItemsOutcomeDao : BaseDao<ItemOutcome>, BasePagingDao<ItemOutcome>
interface IUnitsDao : BaseDao<Unit>

//interface IValuesDao : BaseDao<Value>
interface IParametersDao : BaseDao<Parameter>
interface IItemsDao : BaseDao<Item>, BasePagingDao<Item>