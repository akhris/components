@file:OptIn(ExperimentalDecomposeApi::class)

package ui.screens.entities_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import strings.StringProvider
import ui.screens.entities_screen.entities_filter.EntitiesFilterUi
import ui.screens.entities_screen.entities_grouping.EntitiesGroupingUi
import ui.screens.entities_screen.entities_search.EntitiesSearchUi
import ui.screens.entities_screen.entities_selector.EntitiesSelectorUi
import ui.screens.entities_screen.entities_view_settings.EntitiesViewSettingsUi
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType
import ui.screens.patterns.ScreenWithFilterSheet

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
fun EntitiesScreenUi(component: IEntitiesScreen, stringProvider: StringProvider) {

    val state by remember(component) { component.state }.subscribeAsState()

    val itemsCount = remember(state) { state.itemsCount }
    val itemRepresentationType = remember(state) { state.itemRepresentationType }
    val listRouterState = remember(component) { component.listRouterState }
    val viewSettingsRouterState = remember(component) { component.viewSettingsRouterState }
    val selectorRouterState = remember(component) { component.selectorRouterState }
    val filterRouterState = remember(component) { component.filterRouterState }
    val searchRouterState = remember(component) { component.searchRouterState }
    val groupingRouterState = remember(component) { component.groupingRouterState }

    ScreenWithFilterSheet(
        isOpened = true,
        isModal = true,
        content = {
            ListPane(listRouterState, itemRepresentationType = itemRepresentationType, stringProvider = stringProvider)
        },
        filterSheetTitle = itemsCount?.let {
            {
                Text(modifier = Modifier.padding(8.dp), text = "total: $itemsCount")
            }
        },
        filterContent = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(horizontal = 8.dp)) {
                ViewModeSelector(viewSettingsRouterState)
                SelectorPanel(selectorRouterState, stringProvider = stringProvider)
                SearchPanel(searchRouterState)
                GroupingPanel(groupingRouterState)
                FilterPanel(filterRouterState)
            }
        },
        mainScreenTitle = {
            ListItem(
                text = { state.screenTitle?.let { Text(stringProvider.getLocalizedString(id = it.name)) } },
                secondaryText = { state.screenDescription?.let { Text(stringProvider.getLocalizedString(it.name)) } }
            )
        }
    )
}

@Composable
fun GroupingPanel(routerState: Value<RouterState<*, IEntitiesScreen.EntitiesGroupingChild>>) {
    Children(routerState) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesGroupingChild.EntitiesGrouping -> {
                EntitiesGroupingUi(component = child.component)
            }
        }
    }
}


@Composable
private fun ListPane(
    routerState: Value<RouterState<*, IEntitiesScreen.ListChild>>,
    itemRepresentationType: ItemRepresentationType,
    stringProvider: StringProvider
) {
    Children(routerState) {
        when (val child = it.instance) {
            is IEntitiesScreen.ListChild.List<*> -> {
                log("create list ui for $child")
                ui.screens.entities_screen.entities_list.EntitiesListUi(
                    component = child.component,
                    itemRepresentationType = itemRepresentationType,
                    stringProvider = stringProvider
                )
            }
        }
    }
}

@Composable
private fun SelectorPanel(
    routerState: Value<RouterState<*, IEntitiesScreen.EntitiesSelectorChild>>,
    stringProvider: StringProvider
) {
    Children(routerState, animation = childAnimation(fade())) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesSelectorChild.EntitiesSelector -> {
                EntitiesSelectorUi(component = child.component, stringProvider = stringProvider)
            }
        }
    }
}

@Composable
private fun FilterPanel(routerState: Value<RouterState<*, IEntitiesScreen.EntitiesFilterChild>>) {
    Children(routerState, animation = childAnimation(fade())) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesFilterChild.EntitiesFilter -> {
                EntitiesFilterUi(component = child.component)
            }
        }
    }
}

@Composable
private fun SearchPanel(routerState: Value<RouterState<*, IEntitiesScreen.EntitiesSearchChild>>) {
    Children(routerState, animation = childAnimation(fade())) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesSearchChild.EntitiesSearch -> {
                EntitiesSearchUi(component = child.component)
            }
        }
    }
}


@Composable
private fun ViewModeSelector(routerState: Value<RouterState<*, IEntitiesScreen.ViewSettingsChild>>) {
    Children(routerState, animation = childAnimation(fade())) {
        when (val child = it.instance) {
            is IEntitiesScreen.ViewSettingsChild.ViewSettings -> {
                EntitiesViewSettingsUi(component = child.component)
            }
        }
    }
}