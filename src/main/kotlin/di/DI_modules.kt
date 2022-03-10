package di

import domain.entities.Container
import domain.entities.ItemIncome
import domain.entities.Parameter
import domain.entities.Supplier
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.datasources.BaseDao
import persistence.datasources.BasePagingDao
import persistence.datasources.exposed.*
import persistence.repository.*

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
        getRepo = { ItemOutcomeTestRepository() })

val itemsModule =
    getEntityModule(
        "items module",
        getRepo = { ItemsTestRepository() })

val parametersModule = getEntityModule(
    "parameters module",
    getRepo = { BaseRepository<Parameter>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Parameter>> { ParametersDao() }
    })

val projectModule = getEntityModule(
    "project module",
    getRepo = { ProjectTestRepository() })

val suppliersModule = getEntityModule(
    "suppliers module",
    getRepo = { BaseRepository<Supplier>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Supplier>> { SuppliersDao() }
    })

val objectTypesModule = getEntityModule(
    "type objects module",
    getRepo = { ObjectTypesTestRepository() })

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