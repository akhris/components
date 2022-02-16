package ui.screens.projects_screen_with_selector

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.patterns.ScreenWithFilterSheet
import ui.screens.projects_screen_with_selector.project_details.ProjectDetailsUi
import ui.screens.projects_screen_with_selector.projects_selector.ProjectsSelectorUi

@Composable
fun ProjectsWithSelectorUi(component: IProjectsWithSelector) {
    ScreenWithFilterSheet(
        isOpened = true,
        isModal = true,
        content = {
            DetailsPane(component.detailsRouterState)
        },
        filterContent = {
            SelectorPane(component.selectorRouterState)
        }
    )
}



@Composable
private fun DetailsPane(routerState: Value<RouterState<*, IProjectsWithSelector.DetailsChild>>) {
    Children(routerState) {
        when (val child = it.instance) {
            is IProjectsWithSelector.DetailsChild.Details -> {
                ProjectDetailsUi(component = child.component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun SelectorPane(routerState: Value<RouterState<*, IProjectsWithSelector.SelectorChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is IProjectsWithSelector.SelectorChild.Selector -> {
                ProjectsSelectorUi(component = child.component)
            }
        }
    }
}