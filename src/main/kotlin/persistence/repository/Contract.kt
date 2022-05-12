package persistence.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.IRepositoryCallback
import com.akhris.domain.core.repository.ISpecification
import domain.entities.*
import domain.entities.Unit
import persistence.datasources.ListItem
import persistence.datasources.SliceValue

interface IPagingRepository {
    suspend fun getItemsCount(specification: ISpecification): Long
}

interface ISlicingRepository {
    suspend fun getSlice(columnName: String, otherSlices: List<SliceValue<Any>> = listOf()): List<SliceValue<*>>
}

interface IGroupingRepository<ID, ENTITY : IEntity<ID>> {
    suspend fun gQuery(specification: ISpecification): List<ListItem<ENTITY>>
}

/*

     data: [{
        key: "Group 1",
        items: [ ... ],
        count: 3,
    },{
        key: "Group 2",
        items: [ ... ],
        count: 9,
    } ...]



 */


interface IItemsRepository : IRepository<String, Item>, IRepositoryCallback<Item>
interface ITypesRepository : IRepository<String, ObjectType>, IRepositoryCallback<ObjectType>
interface IUnitsRepository : IRepository<String, Unit>, IRepositoryCallback<Unit>
interface IParametersRepository : IRepository<String, Parameter>, IRepositoryCallback<Parameter>
interface IContainersRepository : IRepository<String, Container>, IRepositoryCallback<Container>
interface ISuppliersRepository : IRepository<String, Supplier>, IRepositoryCallback<Supplier>
interface IItemIncomeRepository : IRepository<String, ItemIncome>, IRepositoryCallback<ItemIncome>, IPagingRepository
interface IItemOutcomeRepository : IRepository<String, ItemOutcome>, IRepositoryCallback<ItemOutcome>
interface IProjectRepository : IRepository<String, Project>, IRepositoryCallback<Project>
interface IWarehouseItemRepository : IRepository<String, WarehouseItem>,IGroupingRepository<String, WarehouseItem>