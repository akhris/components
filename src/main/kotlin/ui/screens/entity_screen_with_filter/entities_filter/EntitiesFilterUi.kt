package ui.screens.entity_screen_with_filter.entities_filter

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun EntitiesFilterUi(component: IEntitiesFilter) {
    val state by component.state.subscribeAsState()
    Text(text = "Entities filter ui with component: $component")

}