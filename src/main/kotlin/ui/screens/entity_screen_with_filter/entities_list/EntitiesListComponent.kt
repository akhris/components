package ui.screens.entity_screen_with_filter.entities_list


import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import ui.screens.entity_screen_with_filter.entities_filter.IEntitiesFilter

class EntitiesListComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    filterModel: Value<IEntitiesFilter.Model>,
    private val onListModelChanged: (IEntitiesList.Model<T>) -> Unit
) :
    IEntitiesList<T>,
    ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesList.Model<T>(listOf()))

    override val state: Value<IEntitiesList.Model<T>> = _state

    override fun setEntitiesList(entities: List<T>) {
        _state.reduce {
            it.copy(entities = entities)
        }
        onListModelChanged(_state.value)
    }

    init {
        filterModel.subscribe {
            println("filter model changes: $it in $this")
            //todo apply filtering to the list of entities here
        }
    }


}