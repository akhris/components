package di

import domain.application.*
import domain.entities.Supplier
import domain.repository.ISuppliersRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.SuppliersTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val suppliersModule = DI.Module("suppliers module") {

    bindSingleton<ISuppliersRepository> { SuppliersTestRepository() }
    bindSingleton { GetSupplier(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateSupplier(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertSupplier(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveSupplier(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetSuppliersList(instance(), ioDispatcher = Dispatchers.IO) }

    bindSingleton<SingleEntityViewModel<String, Supplier>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, Supplier>> {
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