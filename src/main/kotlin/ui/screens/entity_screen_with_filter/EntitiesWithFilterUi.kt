package ui.screens.entity_screen_with_filter

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entity_screen_with_filter.entities_filter.EntitiesFilterUi
import ui.screens.entity_screen_with_filter.entities_list.EntitiesListUi
import ui.screens.patterns.ScreenWithFilterSheet

@Composable
fun EntitiesWithFilterUi(component: IEntityWithFilter) {


    ScreenWithFilterSheet(
        content = {
            ListPane(component.listRouterState)
        },
        filterContent = {
            FilterPane(component.filterRouterState)
        }
    )
}


@Composable
private fun ListPane(routerState: Value<RouterState<*, IEntityWithFilter.ListChild>>) {
    Children(routerState) {
        when (val child = it.instance) {
            is IEntityWithFilter.ListChild.List<*> -> {
                EntitiesListUi(component = child.component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun FilterPane(routerState: Value<RouterState<*, IEntityWithFilter.FilterChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IEntityWithFilter.FilterChild.Filter -> {
                EntitiesFilterUi(component = child.component)
            }
        }
    }
}