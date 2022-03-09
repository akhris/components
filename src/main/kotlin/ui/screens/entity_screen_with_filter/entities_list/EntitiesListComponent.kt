package ui.screens.entity_screen_with_filter.entities_list


import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import com.akhris.domain.core.utils.unpack
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import persistence.repository.IPagingRepository
import kotlinx.coroutines.*
import persistence.repository.Specification
import ui.screens.entity_screen_with_filter.entities_filter.IEntitiesFilter
import ui.screens.entity_screen_with_filter.entities_filter.getSpecification

class EntitiesListComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    private val filterModel: Value<IEntitiesFilter.Model>,
    private val onListModelChanged: (IEntitiesList.Model<T>) -> Unit,
    private val getEntities: GetEntities<*, out T>
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
        onListModelChanged(_state.value)
    }

    private fun invalidateEntities() {
        scope.launch {
            val entities = getEntities(GetEntities.GetBySpecification(Specification.QueryAll)).unpack()
            _state.reduce {
                it.copy(entities = entities)
            }
            onListModelChanged(_state.value)
        }
    }

    init {
        lifecycle.subscribe(onDestroy = {
            filterModel.unsubscribe {

            }
            scope.coroutineContext.cancelChildren()
        })

        filterModel.subscribe { fm ->
            log("filter model changes: $fm in $this")
            //todo apply filtering to the list of entities here
            _state.reduce {
                it.copy(itemRepresentationType = fm.itemRepresentationType)
            }

        }
        setupPagination()
        invalidateEntities()
    }

    //todo get paging initial parameters here:
    private fun setupPagination() {
        val repo = (getEntities.repo as? IPagingRepository) ?: return

        scope.launch {
            val totalItems = repo.getItemsCount(filterModel.value.getSpecification())
            setTotalItems(totalItems)
        }
    }

    private fun setTotalItems(totalItems: Long) {
        _state.reduce {
            it.copy(pagingParameters = it.pagingParameters.copy(totalItems = totalItems))
        }
    }

    override fun setCurrentPage(currentPage: Long) {
        _state.reduce {
            it.copy(pagingParameters = it.pagingParameters.copy(currentPage = currentPage))
        }
    }

    override fun setItemsPerPage(itemsPerPage: Long) {
        _state.reduce {
            it.copy(pagingParameters = it.pagingParameters.copy(itemsPerPage = itemsPerPage))
        }
    }


}