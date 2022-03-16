package ui.screens.entities_screen.entities_selector

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.composable.RepresentationTypesSelector
import ui.screens.entities_screen.title
import utils.toLocalizedString
import kotlin.reflect.KClass

@Composable
fun EntitiesSelectorUi(component: IEntitiesSelector) {
    val state by component.state.subscribeAsState()

    Column {

        RepresentationTypesSelector(
            currentType = state.itemRepresentationType,
            onTypeChanged = { component.changeItemRepresentationType(it) })

        //entities selector:
        state.entitiesSelector?.let {
            EntitiesSelector(
                entities = it.items,
                selection = it.selection,
                onSelectionChanged = { e -> component.selectEntity(e) }
            )
        }
    }
}


@Composable
private fun EntitiesSelector(
    entities: List<KClass<out IEntity<*>>>,
    selection: KClass<out IEntity<*>>,
    onSelectionChanged: (KClass<out IEntity<*>>) -> Unit
) {

    Column {
        entities.forEachIndexed { index, entity ->
            val background = when (entity == selection) {
                true -> MaterialTheme.colors.primary
                false -> MaterialTheme.colors.surface
            }


            Text(
                modifier = Modifier.fillMaxWidth()
                    .selectable(
                        selected = entity == selection,
                        onClick = { onSelectionChanged(entity) },
                        role = Role.RadioButton
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                background,
                                MaterialTheme.colors.surface
                            )
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp),
                text = entity.title?.toLocalizedString() ?: entity.simpleName ?: entity.toString(),
                color = MaterialTheme.colors.contentColorFor(background)
            )

            if (index != entities.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp), startIndent = 4.dp)
            }
        }
    }
}

