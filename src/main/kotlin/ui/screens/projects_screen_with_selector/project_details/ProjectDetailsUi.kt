package ui.screens.projects_screen_with_selector.project_details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.composable.ScrollableBox
import ui.screens.entity_renderers.RenderCardEntity


@Composable
fun ProjectDetailsUi(component: IProjectDetails) {
    val state by component.state.subscribeAsState()


    ScrollableBox(modifier = Modifier.fillMaxWidth()) {
        state.project?.let {

            RenderCardEntity(
                it,
                onEntityChanged = {
//            onEntityUpdated?.invoke(it)
                },
                onEntityRemoved =
                {
//            onEntityRemoved
                }
            )

        }
    }
}