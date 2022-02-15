package di

import domain.application.*
import domain.entities.ItemOutcome
import domain.repository.IItemOutcomeRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ItemOutcomeTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val itemOutcomeModule = DI.Module("item outcome module") {

    bindSingleton<IItemOutcomeRepository> { ItemOutcomeTestRepository() }
    bindSingleton { GetItemOutcome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateItemOutcome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveItemOutcome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertItemOutcome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetItemOutcomesList(instance(), ioDispatcher = Dispatchers.IO) }

    bindSingleton<SingleEntityViewModel<String, ItemOutcome>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, ItemOutcome>> {
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