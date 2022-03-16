package ui.screens.nav_host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.entities_screen.EntitiesScreenUi
import ui.screens.settings.SettingsUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun NavHostUi(component: INavHost) {

    val routerState by component.routerState.subscribeAsState()

    Children(routerState = routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is INavHost.Child.Settings -> SettingsUi(child.component)
            is INavHost.Child.EntitiesListWithSidePanel -> EntitiesScreenUi(child.component)
        }
    }
}