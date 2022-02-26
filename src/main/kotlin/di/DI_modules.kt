package di

import org.kodein.di.instance
import persistence.repository.*

val containersModule = getEntityModule(
    "containers module",
    getRepo = { ContainersTestRepository() },
    getRepoCallbacks = { ContainersTestRepository() })

val itemIncomeModule = getEntityModule(
    "item income module",
    getRepoCallbacks = { ItemIncomeTestRepository() },
    getRepo = { ItemIncomeTestRepository() })

val itemOutcomeModule =
    getEntityModule(
        "item outcome module",
        getRepoCallbacks = { ItemOutcomeTestRepository() },
        getRepo = { ItemOutcomeTestRepository() })

val itemsModule =
    getEntityModule(
        "items module",
        getRepoCallbacks = { ItemsTestRepository() },
        getRepo = { ItemsTestRepository() })

val parametersModule = getEntityModule(
    "parameters module",
    getRepoCallbacks = { ParametersTestRepository() },
    getRepo = { ParametersTestRepository() })

val projectModule = getEntityModule(
    "project module",
    getRepoCallbacks = { ProjectTestRepository() },
    getRepo = { ProjectTestRepository() })

val suppliersModule = getEntityModule(
    "suppliers module",
    getRepoCallbacks = { SuppliersTestRepository() },
    getRepo = { SuppliersTestRepository() })

val objectTypesModule = getEntityModule(
    "type objects module",
    getRepoCallbacks = { ObjectTypesTestRepository() },
    getRepo = { ObjectTypesTestRepository() })

val warehouseItemModule =
    getEntityModule(
        "warehouse module",
        getRepo = { WarehouseItemRepository(instance(), instance()) })

val unitsModule = getEntityModule(
    "units module",
    getRepoCallbacks = { UnitsTestRepository() },
    getRepo = { UnitsTestRepository() })