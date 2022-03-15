package ui.screens.entities_screen

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.entities_screen.entities_sidepanel.SidePanelUi
import ui.screens.patterns.ScreenWithFilterSheet

@Composable
fun EntitiesScreenUi(component: IEntitiesScreen) {
    ScreenWithFilterSheet(
        isModal = true,
        content = {
            ListPane(component.listRouterState)
        },
        filterContent = {
            SidePanel(component.sidePanelRouterState)
        }
    )
}


@Composable
private fun ListPane(routerState: Value<RouterState<*, IEntitiesScreen.ListChild>>) {
    Children(routerState) {
        when (val child = it.instance) {
            is IEntitiesScreen.ListChild.List<*> -> {
                ui.screens.entities_screen.entities_list.EntitiesListUi(component = child.component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun SidePanel(routerState: Value<RouterState<*, IEntitiesScreen.SidePanelChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IEntitiesScreen.SidePanelChild.SidePanel -> {
                SidePanelUi(component = child.component)
            }
        }
    }
}