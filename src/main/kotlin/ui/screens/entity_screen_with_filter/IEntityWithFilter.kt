package ui.screens.entity_screen_with_filter

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entity_screen_with_filter.entities_filter.IEntitiesFilter
import ui.screens.entity_screen_with_filter.entities_list.IEntitiesList

interface IEntityWithFilter{

    val listRouterState: Value<RouterState<*, ListChild>>
    val filterRouterState: Value<RouterState<*, FilterChild>>

    sealed class ListChild{
        class List<T : IEntity<*>>(val component: IEntitiesList<T>) : ListChild()
    }

    sealed class FilterChild {
        data class Filter(val component: IEntitiesFilter) : FilterChild()
    }

}