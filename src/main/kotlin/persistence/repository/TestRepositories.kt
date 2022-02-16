package persistence.repository

import domain.entities.*
import domain.entities.Unit
import domain.repository.*

class ContainersTestRepository : BaseEntityTestRepository<String, Container>(), IContainersRepository
class ItemsTestRepository : BaseEntityTestRepository<String, Item>(), IItemsRepository
class ParametersTestRepository : BaseEntityTestRepository<String, Parameter>(), IParametersRepository
class ObjectTypesTestRepository : BaseEntityTestRepository<String, ObjectType>(), ITypesRepository
class UnitsTestRepository : BaseEntityTestRepository<String, Unit>(), IUnitsRepository
class SuppliersTestRepository : BaseEntityTestRepository<String, Supplier>(), ISuppliersRepository
class ItemIncomeTestRepository : BaseEntityTestRepository<String, ItemIncome>(), IItemIncomeRepository
class ItemOutcomeTestRepository : BaseEntityTestRepository<String, ItemOutcome>(), IItemOutcomeRepository
class ProjectTestRepository : BaseEntityTestRepository<String, Project>(), IProjectRepository