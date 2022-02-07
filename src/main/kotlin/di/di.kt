package di

import persistence.repository.ItemsTestRepository
import persistence.repository.ObjectTypesTestRepository
import domain.application.*
import domain.entities.ObjectType
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.fieldsmappers.IFieldsMapperFactory
import domain.repository.IItemsRepository
import domain.repository.ITypesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ui.settings.AppSettingsRepository
import viewmodels.SingleEntityViewModel

val di = DI {
    bindSingleton<IItemsRepository> { ItemsTestRepository() }
    bindSingleton { GetItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetItemsList(instance(), ioDispatcher = Dispatchers.IO) }


    bindSingleton<ITypesRepository> { ObjectTypesTestRepository() }
    bindSingleton { GetObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetObjectTypes(instance(), ioDispatcher = Dispatchers.IO) }


    bindSingleton<CoroutineScope> { MainScope() }
    bindSingleton { AppSettingsRepository(instance()) }

    bindSingleton<IFieldsMapperFactory> { FieldsMapperFactory() }

    bindSingleton<SingleEntityViewModel<String, ObjectType>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
}
