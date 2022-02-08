package di

import domain.application.*
import domain.entities.Container
import domain.repository.IContainersRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ContainersTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val containersModule = DI.Module("containers module") {

    bindSingleton<IContainersRepository> { ContainersTestRepository() }
    bindSingleton { GetContainer(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateContainer(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveContainer(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertContainer(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetContainersList(instance(), ioDispatcher = Dispatchers.IO) }

    bindSingleton<SingleEntityViewModel<String, Container>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, Container>> {
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