package di

import domain.application.*
import domain.entities.Item
import domain.repository.IItemsRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ItemsTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val itemsModule = DI.Module("items module"){

    bindSingleton<IItemsRepository> { ItemsTestRepository() }
    bindSingleton { GetItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertItem(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetItemsList(instance(), ioDispatcher = Dispatchers.IO) }

    bindSingleton<SingleEntityViewModel<String, Item>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, Item>> {
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