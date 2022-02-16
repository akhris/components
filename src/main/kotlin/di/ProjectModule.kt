package di

import domain.application.*
import domain.entities.Project
import domain.repository.IProjectRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.repository.ProjectTestRepository
import viewmodels.MultipleEntitiesViewModel
import viewmodels.SingleEntityViewModel

val projectModule = DI.Module("project module") {

    bindSingleton<IProjectRepository> { ProjectTestRepository() }
    bindSingleton {    GetProject(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateProject(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveProject(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertProject(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton {    GetProjectsList(instance(), ioDispatcher = Dispatchers.IO) }

    bindSingleton<SingleEntityViewModel<String, Project>> {
        SingleEntityViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindSingleton<MultipleEntitiesViewModel<String, Project>> {
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