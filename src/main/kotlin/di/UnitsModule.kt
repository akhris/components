package di

import domain.application.*
import domain.entities.Unit
import domain.repository.IUnitsRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.UnitsTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val unitsModule = DI.Module("units module"){
    bindSingleton<IUnitsRepository> { UnitsTestRepository() }

    bindSingleton { GetUnit(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertUnit(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateUnit(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveUnit(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetUnits(instance(), ioDispatcher = Dispatchers.IO) }


    bindSingleton<SingleEntityViewModel<String, Unit>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, Unit>> {
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