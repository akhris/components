package persistence.datasources

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import domain.entities.ItemIncome
import domain.entities.ItemOutcome
import domain.entities.Parameter
import domain.entities.Unit

interface IBaseDao<T : IEntity<*>> {
    suspend fun getByID(id: String): T?
//    suspend fun getAll(filters: List<FilterSpec>): List<T>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun removeById(id: String)

    suspend fun query(
        filterSpec: ISpecification? = null,
        sortingSpec: ISpecification? = null,
        pagingSpec: ISpecification? = null
    ): List<T>

    suspend fun getItemsCount(
        filterSpec: ISpecification? = null,
        sortingSpec: ISpecification? = null,
        pagingSpec: ISpecification? = null
    ): Long
}

interface IItemsIncomeDao : IBaseDao<ItemIncome>
interface IItemsOutcomeDao : IBaseDao<ItemOutcome>
interface IUnitsDao : IBaseDao<Unit>

//interface IValuesDao : BaseDao<Value>
interface IParametersDao : IBaseDao<Parameter>