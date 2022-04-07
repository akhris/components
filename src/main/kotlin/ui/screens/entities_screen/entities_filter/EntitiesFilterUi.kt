package ui.screens.entities_screen.entities_filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.entities.fieldsmappers.EntityFieldID
import ui.composable.ChipGroup
import ui.composable.FilterChip
import utils.replace

@Composable
fun EntitiesFilterUi(component: IEntitiesFilter) {
    val state by remember(component) { component.state }.subscribeAsState()


    Column {

        Text(modifier = Modifier.padding(8.dp), text = "filter", style = MaterialTheme.typography.subtitle2)

        var openedParentChips by remember { mutableStateOf(listOf<EntityFieldID>()) }

        ChipGroup {
            state.filters.forEach { fs ->
                val filteredItems = fs.fieldsList.count { it.isFiltered }
                FilterChip(
                    text = "${fs.fieldID.name} (${filteredItems}/${fs.fieldsList.size})",
                    isSelected = filteredItems > 0,
                    onClick = {
                        openedParentChips = if (fs.fieldID in openedParentChips) {
                            openedParentChips - fs.fieldID
                        } else {
                            openedParentChips + fs.fieldID
                        }
                    },
                    withBorder = true,
                    withCheckIcon = false
                )
                if (fs.fieldID in openedParentChips && fs.fieldsList.isNotEmpty()) {
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