package domain.repository

import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.IRepositoryCallback
import domain.entities.*
import domain.entities.Unit

interface IItemsRepository : IRepository<String, Item>, IRepositoryCallback<Item>
interface ITypesRepository : IRepository<String, ObjectType>, IRepositoryCallback<ObjectType>
interface IUnitsRepository : IRepository<String, Unit>, IRepositoryCallback<Unit>
interface IParametersRepository : IRepository<String, Parameter>, IRepositoryCallback<Parameter>
interface IContainersRepository : IRepository<String, Container>, IRepositoryCallback<Container>
interface ISuppliersRepository : IRepository<String, Supplier>, IRepositoryCallback<Supplier>
interface IItemIncomeRepository : IRepository<String, ItemIncome>, IRepositoryCallback<ItemIncome>