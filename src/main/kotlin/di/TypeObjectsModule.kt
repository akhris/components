package di

import domain.application.*
import domain.entities.ObjectType
import domain.repository.ITypesRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ObjectTypesTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val objectTypesModule = DI.Module("type objects module"){
    bindSingleton<ITypesRepository> { ObjectTypesTestRepository() }

    bindSingleton { GetObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertObjectType(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetObjectTypes(instance(), ioDispatcher = Dispatchers.IO) }


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

    bindSingleton<MultipleEntitiesViewModel<String, ObjectType>> {
        MultipleEntitiesViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
}