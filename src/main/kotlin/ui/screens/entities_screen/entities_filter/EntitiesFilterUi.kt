package ui.screens.entities_screen.entities_filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import ui.composable.Icons
import utils.replace

@Composable
fun EntitiesFilterUi(component: IEntitiesFilter) {
    val state by remember(component) { component.state }.subscribeAsState()

    Column {

        Row {
            Text(modifier = Modifier.padding(8.dp).weight(1f), text = "filter", style = MaterialTheme.typography.subtitle2)
            Icons.ClearIcon(modifier = Modifier.clickable {
                //clear filters
                component.clearFilters()
            })
        }
        var openedParentChips by remember { mutableStateOf(listOf<EntityFieldID>()) }

        ChipGroup {
            state.filters.forEach { fs ->
                when (fs) {
                    is IEntitiesFilter.Filter.Range -> RenderFilterRange(fs)
                    is IEntitiesFilter.Filter.Values -> RenderFilterValues(
                        fs,
                        showFilters = fs.fieldID in openedParentChips,
                        onTitleChipClicked = {
                            openedParentChips = if (fs.fieldID in openedParentChips) {
                                openedParentChips - fs.fieldID
                            } else {
                                openedParentChips + fs.fieldID
                            }
                        },
                        onFilterChipClicked = { ff ->
                            component.setFilter(fs.copy(fieldsList = fs.fieldsList.replace(ff.copy(isFiltered = !ff.isFiltered)) {
                                it.value == ff.value
                            }))
                        })
                }


            }
        }
    }

}

@Composable
fun RenderFilterValues(
    filter: IEntitiesFilter.Filter.Values,
    showFilters: Boolean,
    onTitleChipClicked: () -> Unit,
    onFilterChipClicked: (IEntitiesFilter.FilteringValue) -> Unit
) {
    val filteredItems = filter.fieldsList.count { it.isFiltered }

    FilterChip(
        text = "${filter.fieldID.name} (${filteredItems}/${filter.fieldsList.size})",
        isSelected = filteredItems > 0,
        onClick = onTitleChipClicked,
        withBorder = true,
        withCheckIcon = false
    )
    if (showFilters) {
        ChipGroup {
            filter.fieldsList.forEach { ff ->
                FilterChip(
                    text = ff.value.name.toString().ifEmpty { "..." },
                    withCheckIcon = true,
                    withBorder = false,
                    color = MaterialTheme.colors.primaryVariant,
                    isSelected = ff.isFiltered,
                    onClick = {
                        onFilterChipClicked(ff)
                    }
                )
            }
        }
    }
}

@Composable
fun RenderFilterRange(filter: IEntitiesFilter.Filter.Range) {
    Text("composing range from: ${filter.from} to: ${filter.to}")
}
