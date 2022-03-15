package ui.screens.nav_host

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entities_screen.IEntitiesScreen
import ui.screens.entity_screen_with_filter.IEntityWithFilter
import ui.screens.projects_screen_with_selector.IProjectsWithSelector
import ui.screens.settings.ISettings
import ui.screens.types_of_data.ITypesOfData

/**
 * Interface for Navigation Host
 */
interface INavHost {

    /**
     * Navigate to destination by route.
     */
    fun setDestination(route: String)

    /**
     * Exposes Router State
     */
    val routerState: Value<RouterState<*, Child>>

    /**
     * Child classes containing child components.
     */
    sealed class Child {
        class Settings(val component: ISettings) : Child()
        class TypesOfData(val component: ITypesOfData) : Child()
        class EntitiesListWithFilter(val component: IEntityWithFilter) : Child()
        class EntitiesListWithSidePanel(val component: IEntitiesScreen) : Child()
        class Projects(val component: IProjectsWithSelector) : Child()
    }
}