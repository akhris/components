package ui.screens.entities_screen.entities_list

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.Result
import com.akhris.domain.core.application.UpdateEntity
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepositoryCallback
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.*
import persistence.repository.IPagingRepository
import persistence.repository.Specification

class EntitiesListComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
//    private val sidePanelModel: Value<IEntitiesSidePanel.Model>,
//    private val onListModelChanged: (IEntitiesList.Model<T>) -> Unit,
    private val getEntities: GetEntities<*, out T>?,
    private val updateEntity: UpdateEntity<*, out T>?,
    private val removeEntity: RemoveEntity<*, out T>?,
    private val onEntitiesLoaded: (List<T>) -> Unit
) :
    IEntitiesList<T>,
    ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state = MutableValue(IEntitiesList.Model<T>(listOf()))

    override val state: Value<IEntitiesList.Model<T>> = _state

    override fun setEntitiesList(entities: List<T>) {
        _state.reduce {
            it.copy(entities = entities)
        }
//        onListModelChanged(_state.value)
    }

    override fun setCurrentPage(currentPage: Long) {
        _state.reduce {
            it.copy(
                pagingParameters = it.pagingParameters?.copy(currentPage = currentPage)
                    ?: IEntitiesList.PagingParameters(currentPage = currentPage)
            )
        }
    }

    override fun setItemsPerPage(itemsPerPage: Long) {
        _state.reduce {
            it.copy(
                pagingParameters = it.pagingParameters?.copy(itemsPerPage = itemsPerPage)
                    ?: IEntitiesList.PagingParameters(itemsPerPage = itemsPerPage)
            )
        }
    }

    private fun setTotalItems(totalItems: Long) {
        _state.reduce {
            it.copy(
                pagingParameters = it.pagingParameters?.copy(totalItems = totalItems) ?: IEntitiesList.PagingParameters(
                    totalItems = totalItems
                )
            )
        }
    }

    private suspend fun setupPagination() {
        val repo = (getEntities?.repo as? IPagingRepository) ?: return
        val totalItems = try {
            repo.getItemsCount(Specification.QueryAll)  //todo get spec from filter
        } catch (e: Exception) {
            null
        }
        totalItems?.let { setTotalItems(it) }
    }

    override fun onEntityUpdated(entity: T) {
        scope.launch {
            log("updating entity: $entity")
            val result = updateEntity?.invoke(UpdateEntity.Update(entity))
            log(result ?: "empty result")
        }
    }

    override val onEntityRemovedCallback: ((T) -> Unit)? = removeEntity?.let { re ->
        {
            scope.launch {
                re(params = RemoveEntity.Remove(it))
            }
        }
    }

//    override fun onEntityRemoved(entity: T) {
//        scope.launch {
//            log("removing entity: $entity")
//            val result = removeEntity?.invoke(params = RemoveEntity.Remove(entity))
//            log(result ?: "empty result")
//        }
//    }

    private suspend fun invalidateEntities() {
        val pagingParams = _state.value.pagingParameters
        val specification = if (pagingParams == null) {
            Specification.QueryAll
        } else {
            Specification.Paginated(pageNumber = pagingParams.currentPage, itemsPerPage = pagingParams.itemsPerPage)
        }
        val entitiesResult = getEntities?.invoke(GetEntities.GetBySpecification(specification))

        log("invalidating entities for spec: $specification, entitiesResult: $entitiesResult")
        when (entitiesResult) {
            is Result.Success -> {
                _state.reduce {
                    it.copy(entities = entitiesResult.value)
                }
//                onListModelChanged(_state.value)
                onEntitiesLoaded(entitiesResult.value)
            }
            is Result.Failure -> {
                log(entitiesResult.throwable)
                log(entitiesResult.throwable.stackTraceToString())
            }
        }

    }

    init {
        lifecycle.subscribe(onDestroy = {
            scope.coroutineContext.cancelChildren()
        })

        val repoCallback = (getEntities?.repo as? IRepositoryCallback<T>)

        scope.launch {
            setupPagination()
            invalidateEntities()    //todo subscribe to pagination parameters change
            repoCallback?.updates?.collect {
                log("entities updated: $it")
                invalidateEntities()
            }
        }
    }
}