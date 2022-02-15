package ui.screens.entity_screen_with_filter.entities_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.screens.entity_renderers.EntityScreenContent

@Composable
fun <T : IEntity<*>> EntitiesListUi(component: IEntitiesList<T>) {

    val state by component.state.subscribeAsState()

    EntityScreenContent(
        itemRepresentationType = state.itemRepresentationType,
        entities = state.entities
//        ,
//        onEntityRemoved = component::onEntityRemoved,
//        onEntityUpdated = component::onEntityUpdated
    )

}

