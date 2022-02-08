package di

import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.fieldsmappers.IFieldsMapperFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import strings.IStringsProvider
import strings.RUStringsProvider
import ui.settings.AppSettingsRepository

val di = DI {

    bindSingleton<IStringsProvider> { RUStringsProvider() }




    import(objectTypesModule)
    import(itemsModule)
    import(unitsModule)
    import(parametersModule)

    bindSingleton<CoroutineScope> { MainScope() }
    bindSingleton { AppSettingsRepository(instance()) }

    bindSingleton<IFieldsMapperFactory> { FieldsMapperFactory() }


}
