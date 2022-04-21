package di

import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.columnMappers.ColumnMappersFactory
import settings.AppSettingsRepository
import ui.screens.settings.SettingsMapper

val di = DI {

    import(objectTypesModule)
    import(itemsModule)
    import(unitsModule)
    import(parametersModule)
    import(containersModule)
    import(suppliersModule)
    import(itemIncomeModule)
    import(itemOutcomeModule)
    import(projectModule)
    import(warehouseItemModule)
    import(invoicesModule)
//    import(valuesModule)


    bindSingleton<CoroutineScope> { MainScope() }
    bindSingleton { AppSettingsRepository(instance()) }

    bindSingleton { SettingsMapper() }
    bindSingleton<IGetListUseCaseFactory> {
        GetListUseCaseFactory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<IUpdateUseCaseFactory> {
        UpdateUseCaseFactory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )

    }

    bindSingleton<IGetUseCaseFactory> {
        GetUseCaseFactory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )

    }

    bindSingleton<IInsertUseCaseFactory> {
        InsertUseCaseFactory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<IRemoveUseCaseFactory> {
        RemoveUseCaseFactory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }


    bindSingleton { FieldsMapperFactory() }
    bindSingleton { ColumnMappersFactory() }


}
