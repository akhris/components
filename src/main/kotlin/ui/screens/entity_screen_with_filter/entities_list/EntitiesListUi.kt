package ui.screens.entity_screen_with_filter.entities_list

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.akhris.domain.core.entities.IEntity

@Composable
fun <T : IEntity<*>> EntitiesListUi(component: IEntitiesList<T>) {

    Text(text = "Entities list ui with component: $component")

}

