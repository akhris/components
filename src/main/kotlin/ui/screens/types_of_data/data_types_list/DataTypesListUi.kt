package ui.screens.types_of_data.data_types_list

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.composable.ScrollableBox
import ui.screens.EntityScreenContent

@Composable
fun DataTypesListUi(component: IDataTypesList) {

    val entities by component.state.subscribeAsState()
    val scrollState = remember(entities) { ScrollState(initial = 0) }
    ScrollableBox(
        modifier = Modifier.fillMaxHeight(), scrollState = scrollState, innerHorizontalPadding = 64.dp,
        content = {
            EntityScreenContent(entities.entities, onEntityRemoved = component::onEntityRemoved)
        }
    )
}