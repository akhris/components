package ui.screens.types_of_data.data_types_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.entity_renderers.EntityScreenContent


@Composable
fun DataTypesListUi(component: IDataTypesList) {

    val componentState by component.state.subscribeAsState()
    EntityScreenContent(
        itemRepresentationType = componentState.itemRepresentationType,
        entities = componentState.entities,
        onEntityRemoved = component::onEntityRemoved
    )

}