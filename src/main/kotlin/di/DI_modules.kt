package di

import com.akhris.domain.core.utils.IDUtils
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
        bindSingleton<Copier<Container>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }
    }
)

val itemIncomeModule = getEntityModule(
    "item income module",
    getRepo = { BaseRepository<ItemIncome>(baseDao = instance(), basePagingDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<ItemIncome>> { ItemsIncomeDao() }
        bindSingleton<BasePagingDao<ItemIncome>> { ItemsIncomeDao() }
        bindSingleton<Copier<ItemIncome>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }
    })

val itemOutcomeModule =
    getEntityModule(
        "item outcome module",
        getRepo = { BaseRepository<ItemOutcome>(baseDao = instance(), basePagingDao = instance()) },
        additionalBindings = {
            bindSingleton<BaseDao<ItemOutcome>> { ItemsOutcomeDao() }
            bindSingleton<BasePagingDao<ItemOutcome>> { ItemsOutcomeDao() }
            bindSingleton<Copier<ItemOutcome>> {
                {
                    it.copy(id = IDUtils.newID())
                }
            }
        })

val itemsModule =
    getEntityModule(
        "items module",
        getRepo = { BaseRepository<Item>(baseDao = instance()) },
        additionalBindings = {
            bindSingleton<BaseDao<Item>> { ItemsDao() }
            bindSingleton<Copier<Item>> {
                {
                    it.copy(id = IDUtils.newID())
                }
            }
        })

val parametersModule = getEntityModule(
    "parameters module",
    getRepo = { BaseRepository<Parameter>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Parameter>> { ParametersDao() }
        bindSingleton<Copier<Parameter>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }

    })

val projectModule = getEntityModule(
    "project module",
    getRepo = { BaseRepository<Project>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Project>> { ProjectsDao() }
        bindSingleton<Copier<Project>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }
    })

val suppliersModule = getEntityModule(
    "suppliers module",
    getRepo = { BaseRepository<Supplier>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<Supplier>> { SuppliersDao() }
        bindSingleton<Copier<Supplier>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }
    })

val objectTypesModule = getEntityModule(
    "type objects module",
    getRepo = { BaseRepository<ObjectType>(baseDao = instance()) },
    additionalBindings = {
        bindSingleton<BaseDao<ObjectType>> { ObjectTypesDao() }
        bindSingleton<Copier<ObjectType>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }
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
        bindSingleton<Copier<domain.entities.Unit>> {
            {
                it.copy(id = IDUtils.newID())
            }
        }
    })

//val valuesModule = getEntityModule(
//    "values module",
//    getRepo = { BaseRepository<domain.entities.Value>(baseDao = instance()) },
//    additionalBindings = {
//        bindSingleton<BaseDao<domain.entities.Value>> { ValuesDao() }
//    })