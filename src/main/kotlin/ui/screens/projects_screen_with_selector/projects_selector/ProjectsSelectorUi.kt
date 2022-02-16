package ui.screens.projects_screen_with_selector.projects_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun ProjectsSelectorUi(component: IProjectsSelector) {

    val state by component.state.subscribeAsState()

    Column {

        state.projects.forEachIndexed { index, selectorItem ->
            val background = when (state.selectedItem?.projectID == selectorItem.projectID) {
                true -> MaterialTheme.colors.primary
                false -> MaterialTheme.colors.surface
            }

            Text(
                modifier = Modifier.fillMaxWidth()
                    .selectable(
                        selected = state.selectedItem == selectorItem,
                        onClick = {
                            component.selectProject(
                                selectorItem.projectID
                            )
                        },
                        role = Role.RadioButton
                    )
                    .background(color = background)
                    .padding(8.dp),
                text = selectorItem.projectName,
                color = MaterialTheme.colors.contentColorFor(background)
            )

            if (index != state.projects.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }
        }

    }

}