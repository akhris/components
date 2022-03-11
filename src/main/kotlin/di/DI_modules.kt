package di

import domain.entities.*
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.datasources.BaseDao
import persistence.datasources.BasePagingDao
import persistence.datasources.exposed.*
import persistence.repository.BaseRepository
import persistence.repository.WarehouseItemRepository

val containersModule = getEntityModule(
    "containers module",
    getRepo = { BaseRepository<Container>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Container>> { ContainersDao() }
    }
)

val itemIncomeModule = getEntityModule(
    "item income module",
    getRepo = { BaseRepository<ItemIncome>(baseDao = instance(), basePagingDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<ItemIncome>> { ItemsIncomeDao() }
        bindSingleton<BasePagingDao<ItemIncome>> { ItemsIncomeDao() }
    })

val itemOutcomeModule =
    getEntityModule(
        "item outcome module",
        getRepo = { BaseRepository<ItemOutcome>(baseDao = instance(), basePagingDao = instance()) },
        additionalBindings = {
            bindSingleton<BaseDao<ItemOutcome>> { ItemsOutcomeDao() }
            bindSingleton<BasePagingDao<ItemOutcome>> { ItemsOutcomeDao() }
        })

val itemsModule =
    getEntityModule(
        "items module",
        getRepo = { BaseRepository<Item>(baseDao = instance()) },
        additionalBindings = {
            bindSingleton<BaseDao<Item>> { ItemsDao() }
        })

val parametersModule = getEntityModule(
    "parameters module",
    getRepo = { BaseRepository<Parameter>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Parameter>> { ParametersDao() }
    })

val projectModule = getEntityModule(
    "project module",
    getRepo = { BaseRepository<Project>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Project>> { ProjectsDao() }
    })

val suppliersModule = getEntityModule(
    "suppliers module",
    getRepo = { BaseRepository<Supplier>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Supplier>> { SuppliersDao() }
    })

val objectTypesModule = getEntityModule(
    "type objects module",
    getRepo = { BaseRepository<ObjectType>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<ObjectType>> { ObjectTypesDao() }
    })

val warehouseItemModule =
    getEntityModule(
        "warehouse module",
        getRepo = { WarehouseItemRepository(instance(), instance()) })

val unitsModule = getEntityModule(
    "units module",
    getRepo = { BaseRepository<domain.entities.Unit>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<domain.entities.Unit>> { UnitsDao() }
    })