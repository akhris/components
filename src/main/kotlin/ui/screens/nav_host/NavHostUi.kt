package ui.screens.nav_host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.settings.SettingsUi
import ui.screens.types_of_data.TypesOfDataUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun NavHostUi(component: INavHost) {
    val state by component.state.subscribeAsState()
    val routerState by component.routerState.subscribeAsState()

    println("going to show screen: ${state.destination}")
    Children(routerState = routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is INavHost.Child.Settings -> SettingsUi(child.component)
            is INavHost.Child.TypesOfData -> TypesOfDataUi(child.component)
        }
    }
}