package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import strings.StringsIDs
import ui.screens.entities_screen.entities_filter.IEntitiesFilter
import ui.screens.entities_screen.entities_list.IEntitiesList
import ui.screens.entities_screen.entities_selector.IEntitiesSelector
import ui.screens.entities_screen.entities_view_settings.IEntitiesViewSettings
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType


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
        val screenTitle: StringsIDs? = null,
        val screenDescription: StringsIDs? = null,
        val itemRepresentationType: ItemRepresentationType = ItemRepresentationType.default
    )

    val listRouterState: Value<RouterState<*, ListChild>>
    val selectorRouterState: Value<RouterState<*, EntitiesSelectorChild>>
    val filterRouterState: Value<RouterState<*, EntitiesFilterChild>>
    val viewSettingsRouterState: Value<RouterState<*, ViewSettingsChild>>

    sealed class ListChild {
        data class List<T : IEntity<*>>(val component: IEntitiesList<T>) : ListChild()
    }

    sealed class EntitiesSelectorChild {
        data class EntitiesSelector(val component: IEntitiesSelector) : EntitiesSelectorChild()
    }

    sealed class EntitiesFilterChild {
        data class EntitiesFilter(val component: IEntitiesFilter) : EntitiesFilterChild()
    }

    sealed class ViewSettingsChild {
        data class ViewSettings(val component: IEntitiesViewSettings) : ViewSettingsChild()
    }

}