package ui.screens.entities_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entities_screen.entities_filter.EntitiesFilterUi
import ui.screens.entities_screen.entities_selector.EntitiesSelectorUi
import ui.screens.entities_screen.entities_view_settings.EntitiesViewSettingsUi
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType
import ui.screens.patterns.ScreenWithFilterSheet
import utils.toLocalizedString

/**
 * Ui element: Screen of entities.
 * Contains:
 * - list of entities
 * - view mode selector (cards/table)
 * - entity selector panel
 * - filter panel
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EntitiesScreenUi(component: IEntitiesScreen) {

    val state by component.state.subscribeAsState()

    ScreenWithFilterSheet(
        isOpened = true,
        isModal = true,
        content = {
            ListPane(component.listRouterState, itemRepresentationType = state.itemRepresentationType)
        },
        filterContent = {
            Column {
                ViewModeSelector(component.viewSettingsRouterState)
                SelectorPanel(component.selectorRouterState)
                FilterPanel(component.filterRouterState)
            }
        },
        mainScreenTitle = {
            ListItem(
                text = { state.screenTitle?.toLocalizedString()?.let { Text(it) } },
                secondaryText = { state.screenDescription?.toLocalizedString()?.let { Text(it) } }
            )
        }
    )
}


@Composable
private fun ListPane(
    routerState: Value<RouterState<*, IEntitiesScreen.ListChild>>,
    itemRepresentationType: ItemRepresentationType
) {
    Children(routerState) {
        when (val child = it.instance) {
            is IEntitiesScreen.ListChild.List<*> -> {
                ui.screens.entities_screen.entities_list.EntitiesListUi(
                    component = child.component,
                    itemRepresentationType = itemRepresentationType
                )
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun SelectorPanel(routerState: Value<RouterState<*, IEntitiesScreen.EntitiesSelectorChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesSelectorChild.EntitiesSelector -> {
                EntitiesSelectorUi(component = child.component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun FilterPanel(routerState: Value<RouterState<*, IEntitiesScreen.EntitiesFilterChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesFilterChild.EntitiesFilter -> {
                EntitiesFilterUi(component = child.component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun ViewModeSelector(routerState: Value<RouterState<*, IEntitiesScreen.ViewSettingsChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IEntitiesScreen.ViewSettingsChild.ViewSettings -> {
                EntitiesViewSettingsUi(component = child.component)
            }
        }
    }
}