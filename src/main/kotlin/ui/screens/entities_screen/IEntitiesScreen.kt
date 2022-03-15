package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entities_screen.entities_list.IEntitiesList
import ui.screens.entities_screen.entities_sidepanel.IEntitiesSidePanel


/**
 * Interface for Entity-with-sidepanel component.
 * Consists of two components:
 * 1. Entities List component
 *      Component that fetches a list of entities from repository depending on sidepanel settings
 * 2. SidePanel component
 *      Component that handles filtering/selecting current item
 */
interface IEntitiesScreen{

    val listRouterState: Value<RouterState<*, ListChild>>
    val sidePanelRouterState: Value<RouterState<*, SidePanelChild>>

    sealed class ListChild{
        class List<T : IEntity<*>>(val component: IEntitiesList<T>) : ListChild()
    }

    sealed class SidePanelChild {
        data class SidePanel(val component: IEntitiesSidePanel) : SidePanelChild()
    }

}