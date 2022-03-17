package di

import com.akhris.domain.core.application.*
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

inline fun <reified ID, reified ENTITY : IEntity<ID>> getEntityModule(
    moduleName: String,
    crossinline getRepo: DirectDI.() -> IRepository<ID, ENTITY>,
    crossinline additionalBindings: DI.Builder.() -> Unit = {}
): DI.Module = DI.Module(moduleName) {
    bindSingleton<IRepository<ID, ENTITY>> { getRepo() }
//    bindSingleton<IRepositoryCallback<ENTITY>> { getRepoCallbacks() }
    bindSingleton { GetEntity<ID, ENTITY>(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { UpdateEntity<ID, ENTITY>(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { RemoveEntity<ID, ENTITY>(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { InsertEntity<ID, ENTITY>(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton { GetEntities<ID, ENTITY>(instance(), ioDispatcher = Dispatchers.IO) }

    additionalBindings()
}