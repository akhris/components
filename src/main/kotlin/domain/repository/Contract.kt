package domain.repository

import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.IRepositoryCallback
import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Parameter
import domain.entities.Unit

interface IItemsRepository : IRepository<String, Item>, IRepositoryCallback<Item>
interface ITypesRepository : IRepository<String, ObjectType>, IRepositoryCallback<ObjectType>
interface IUnitsRepository : IRepository<String, Unit>, IRepositoryCallback<Unit>
interface IParametersRepository : IRepository<String, Parameter>, IRepositoryCallback<Parameter>