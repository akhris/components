package di

import domain.application.*
import domain.entities.ItemIncome
import domain.repository.IItemIncomeRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ItemIncomeTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val itemIncomeModule = DI.Module("item income module") {

    bindSingleton<IItemIncomeRepository> { ItemIncomeTestRepository() }
    bindSingleton { GetItemIncome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateItemIncome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveItemIncome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertItemIncome(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetItemIncomesList(instance(), ioDispatcher = Dispatchers.IO) }

    bindSingleton<SingleEntityViewModel<String, ItemIncome>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, ItemIncome>> {
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