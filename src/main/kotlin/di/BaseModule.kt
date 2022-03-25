package di

import com.akhris.domain.core.application.*
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.*

inline fun <reified ID, reified ENTITY : IEntity<ID>> getEntityModule(
    moduleName: String,
    crossinline getRepo: DirectDI.() -> IRepository<ID, ENTITY>,
    crossinline additionalBindings: DI.Builder.() -> Unit = {}
): DI.Module = DI.Module(moduleName) {
    bindSingleton<IRepository<ID, ENTITY>> { getRepo() }
//    bindSingleton<IRepositoryCallback<ENTITY>> { getRepoCallbacks() }
    bindSingleton { GetEntity<ID, ENTITY>(repo = instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateEntity<ID, ENTITY>(repo = instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveEntity<ID, ENTITY>(repo = instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton {
        InsertEntity<ID, ENTITY>(
            repo = instance(),
            entityCopier = instanceOrNull(),
            ioDispatcher = Dispatchers.IO
        )
    }
    bindSingleton { GetEntities<ID, ENTITY>(repo = instance(), ioDispatcher = Dispatchers.IO) }

    additionalBindings()
}


typealias Copier<ENTITY> = (ENTITY) -> ENTITY