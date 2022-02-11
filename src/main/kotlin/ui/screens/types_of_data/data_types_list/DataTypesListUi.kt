package ui.screens.types_of_data.data_types_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.EntityScreenContent

@Composable
fun DataTypesListUi(component: IDataTypesList) {

    val componentState by component.state.subscribeAsState()
    EntityScreenContent(
        componentState.selectedType,
        componentState.entities,
        onEntityRemoved = component::onEntityRemoved
    )
//
//    ScrollableBox(
//        modifier = Modifier.fillMaxHeight(), scrollState = scrollState, innerHorizontalPadding = 64.dp,
//        content = {
//            EntityScreenContent(entities.entities, onEntityRemoved = component::onEntityRemoved)
//        }
//    )
}