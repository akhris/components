package ui.screens.entity_screen_with_filter.entities_filter

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.composable.ChipGroup
import ui.composable.FilterChip
import ui.screens.types_of_data.types_selector.RepresentationTypesSelector
import utils.replace

@Composable
fun EntitiesFilterUi(component: IEntitiesFilter) {
    val state by component.state.subscribeAsState()


    Column {

        RepresentationTypesSelector(
            currentType = state.itemRepresentationType,
            onTypeChanged = { component.changeItemRepresentationType(it) })

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