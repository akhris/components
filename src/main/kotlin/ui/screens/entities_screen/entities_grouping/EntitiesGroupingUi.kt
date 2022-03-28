package ui.screens.entities_screen.entities_grouping

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.composable.ChipGroup
import ui.composable.FilterChip

@Composable
fun EntitiesGroupingUi(component: IEntitiesGrouping) {
    val state by remember(component) { component.state }.subscribeAsState()

    val groupingSettings = remember(state){state.groupingSettings}
    val currentSelection = remember(state) { state.currentGrouping }

    Column {

        Text(modifier = Modifier.padding(8.dp), text = "grouping", style = MaterialTheme.typography.subtitle2)

        ChipGroup {
            groupingSettings.forEach { gs ->
                FilterChip(
                    text = gs.fieldID.name,
                    isSelected = gs == currentSelection,
                    onClick = {
                              component.groupBy(gs.fieldID)
                    },
                    withBorder = true,
                    withCheckIcon = false
                )
            }
        }
    }
}