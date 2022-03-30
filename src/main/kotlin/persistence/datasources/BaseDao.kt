package persistence.datasources

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import domain.entities.ItemIncome
import domain.entities.ItemOutcome
import domain.entities.Parameter
import domain.entities.Unit
import persistence.repository.FilterSpec

interface BaseDao<T : IEntity<*>> {
    suspend fun getByID(id: String): T?
    suspend fun getAll(filters: List<FilterSpec>): List<T>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun removeById(id: String)

    suspend fun query(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<GroupedResult<T>>

    suspend fun getItemsCount(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Long
}

data class GroupedResult<ENTITY : IEntity<*>>(val key: String, val items: List<ENTITY>)


interface IItemsIncomeDao : BaseDao<ItemIncome>
interface IItemsOutcomeDao : BaseDao<ItemOutcome>
interface IUnitsDao : BaseDao<Unit>

//interface IValuesDao : BaseDao<Value>
interface IParametersDao : BaseDao<Parameter>