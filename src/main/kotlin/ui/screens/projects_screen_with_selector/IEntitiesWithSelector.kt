package ui.screens.projects_screen_with_selector

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.projects_screen_with_selector.project_details.IProjectDetails
import ui.screens.projects_screen_with_selector.projects_selector.IProjectsSelector

/**
 * Interface for Entity-with-selector component.
 * Consists of two components:
 * 1. Entities List component
 *      Component that fetches a list of entities from repository depending on selector settings.
 * 2. Selector settings component
 *      Component that handles selection.
 */
interface IEntitiesWithSelector {

    val listRouterState: Value<RouterState<*, ListChild>>
    val selectorRouterState: Value<RouterState<*, SelectorChild>>

    sealed class ListChild{
        class List(val component: IProjectDetails) : ListChild()
    }

    sealed class SelectorChild {
        data class Selector(val component: IProjectsSelector) : SelectorChild()
    }
}