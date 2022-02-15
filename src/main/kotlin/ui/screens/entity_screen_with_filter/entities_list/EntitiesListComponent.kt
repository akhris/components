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
import kotlinx.coroutines.*
import persistence.repository.Specification
import ui.screens.entity_screen_with_filter.entities_filter.IEntitiesFilter

class EntitiesListComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    filterModel: Value<IEntitiesFilter.Model>,
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

        invalidateEntities()
    }


}