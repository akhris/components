package ui.screens.entity_screen_with_filter

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entity_screen_with_filter.entities_filter.IEntitiesFilter
import ui.screens.entity_screen_with_filter.entities_list.IEntitiesList

/**
 * Interface for Entity-with-filter component.
 * Consists of two components:
 * 1. Entities List component
 *      Component that fetches a list of entities from repository depending on filter settings
 * 2. Filter settings component
 *      Component that handles filtering
 */
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