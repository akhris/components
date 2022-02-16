package ui.screens.nav_host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.entity_screen_with_filter.EntitiesWithFilterUi
import ui.screens.projects_screen_with_selector.ProjectsWithSelectorUi
import ui.screens.settings.SettingsUi
import ui.screens.types_of_data.TypesOfDataUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun NavHostUi(component: INavHost) {

    val routerState by component.routerState.subscribeAsState()

    Children(routerState = routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is INavHost.Child.Settings -> SettingsUi(child.component)
            is INavHost.Child.TypesOfData -> TypesOfDataUi(child.component)
            is INavHost.Child.EntitiesListWithFilter -> EntitiesWithFilterUi(child.component)
            is INavHost.Child.Projects -> ProjectsWithSelectorUi(child.component)
        }
    }
}