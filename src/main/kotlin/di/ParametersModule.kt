package di

import domain.application.*
import domain.entities.Parameter
import domain.repository.IParametersRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ParametersTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val parametersModule = DI.Module("parameters module"){
    bindSingleton<IParametersRepository> { ParametersTestRepository() }

    bindSingleton { GetParameter(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertParameter(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateParameter(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveParameter(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetParametersList(instance(), ioDispatcher = Dispatchers.IO) }


    bindSingleton<SingleEntityViewModel<String, Parameter>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, Parameter>> {
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