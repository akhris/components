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
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import strings.LocalizedStrings
import strings.defaultLocalizedStrings
import ui.screens.entities_screen.entities_filter.EntitiesFilterUi
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
fun EntitiesScreenUi(component: IEntitiesScreen, localizedStrings: LocalizedStrings = defaultLocalizedStrings) {


    val state by remember(component) { component.state }.subscribeAsState()

    val itemRepresentationType = remember(state) { state.itemRepresentationType }
    val listRouterState = remember(component){component.listRouterState}
    val viewSettingsRouterState = remember(component){component.viewSettingsRouterState}
    val selectorRouterState = remember(component){component.selectorRouterState}
    val filterRouterState = remember(component){component.filterRouterState}
    val searchRouterState = remember(component){component.searchRouterState}

    log("composing EntitiesScreenUi. component: $component localizedStrings: $localizedStrings")
    ScreenWithFilterSheet(
        isOpened = true,
        isModal = true,
        content = {
            ListPane(listRouterState, itemRepresentationType = itemRepresentationType)
        },
        filterContent = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(horizontal = 8.dp)) {
                ViewModeSelector(viewSettingsRouterState)
                SelectorPanel(selectorRouterState, localizedStrings = localizedStrings)
                SearchPanel(searchRouterState)
                FilterPanel(filterRouterState)
            }
        },
        mainScreenTitle = {
            ListItem(
                text = { state.screenTitle?.let { Text(localizedStrings(it)) } },
                secondaryText = { state.screenDescription?.let { Text(localizedStrings(it)) } }
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
                log("create list ui for $child")
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
private fun SelectorPanel(
    routerState: Value<RouterState<*, IEntitiesScreen.EntitiesSelectorChild>>,
    localizedStrings: LocalizedStrings
) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IEntitiesScreen.EntitiesSelectorChild.EntitiesSelector -> {
                EntitiesSelectorUi(component = child.component, localizedStrings = localizedStrings)
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
private fun SearchPanel(routerState: Value<RouterState<*, IEntitiesScreen.EntitiesSearchChild>>){
    Children(routerState, animation = crossfade()){
        when(val child = it.instance){
            is IEntitiesScreen.EntitiesSearchChild.EntitiesSearch -> {
                EntitiesSearchUi(component = child.component)
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