package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import strings.Strings
import ui.screens.entities_screen.entities_filter.IEntitiesFilter
import ui.screens.entities_screen.entities_list.IEntitiesList
import ui.screens.entities_screen.entities_selector.IEntitiesSelector


/**
 * Interface for Entity-with-sidepanel component.
 * Consists of two components:
 * 1. Entities List component
 *      Component that fetches a list of entities from repository depending on sidepanel settings
 * 2. SidePanel component
 *      Component that handles filtering/selecting current item
 */
interface IEntitiesScreen {

    val state: Value<Model>

    data class Model(
        val screenTitle: Strings? = null,
        val screenDescription: Strings? = null
    )

    val listRouterState: Value<RouterState<*, ListChild>>
    val selectorRouterState: Value<RouterState<*, EntitiesSelectorChild>>
    val filterRouterState: Value<RouterState<*, EntitiesFilterChild>>

    sealed class ListChild {
        class List<T : IEntity<*>>(val component: IEntitiesList<T>) : ListChild()
    }

    sealed class EntitiesSelectorChild {
        data class EntitiesSelector(val component: IEntitiesSelector) : EntitiesSelectorChild()
    }

    sealed class EntitiesFilterChild {
        data class EntitiesFilter(val component: IEntitiesFilter) : EntitiesFilterChild()
    }

}