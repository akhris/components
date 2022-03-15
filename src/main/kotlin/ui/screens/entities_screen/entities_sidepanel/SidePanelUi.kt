package ui.screens.entities_screen.entities_sidepanel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.composable.ChipGroup
import ui.composable.FilterChip
import ui.screens.types_of_data.types_selector.RepresentationTypesSelector
import utils.replace
import kotlin.reflect.KClass

@Composable
fun SidePanelUi(component: IEntitiesSidePanel) {
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

        //filtering
        ChipGroup {
            state.filters.forEach { fs ->
                FilterChip(
                    text = fs.fieldID.name,
                    isSelected = fs.isActive,
                    onClick = {
                        when (fs.isActive) {
                            true -> component.removeFilter(fs)
                            false -> component.setFilter(fs)
                        }
                    },
                    withBorder = true,
                    withCheckIcon = false
                )
                if (fs.isActive && fs.fieldsList.isNotEmpty()) {
                    ChipGroup {

                        fs.fieldsList.forEach { ff ->
                            FilterChip(
                                text = ff.field.toString(),
                                withCheckIcon = true,
                                withBorder = false,
                                color = MaterialTheme.colors.primaryVariant,
                                isSelected = ff.isFiltered,
                                onClick = {
                                    component.setFilter(fs.copy(fieldsList = fs.fieldsList.replace(ff.copy(isFiltered = !ff.isFiltered)) {
                                        it.field == ff.field
                                    }))
                                }
                            )
                        }
                    }
                }
            }
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
        entities.forEach { entity ->
            Text(
                modifier = Modifier.clickable {
                    onSelectionChanged(entity)
                },
                text = entity.simpleName ?: entity.toString(),
                color = if (selection == entity) {
                    MaterialTheme.colors.primary
                } else Color.Unspecified
            )
        }
    }
}