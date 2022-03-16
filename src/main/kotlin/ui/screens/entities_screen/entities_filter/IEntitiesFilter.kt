package ui.screens.entities_screen.entities_filter

import com.arkivanov.decompose.value.Value
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import ui.screens.entities_screen.entities_filter.IEntitiesFilter.FilterSettings

/**
 * Interface for Filter component of Entity-with-filter block.
 *
 * It's state contains of current Filter settings that is implemented as List of [FilterSettings] objects.
 */
interface IEntitiesFilter {
    //current state of the screen
    val state: Value<Model>


    //methods to change the state:
    //change representation (cards/ grid):
//    fun changeItemRepresentationType(itemRepresentationType: ItemRepresentationType)

    // add / update filter setting
    fun setFilter(filterSettings: FilterSettings)

    // remove filter setting
    fun removeFilter(filterSettings: FilterSettings)

    //filter screen model:
    data class Model(
        //cards / grid
//        val itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Card,

        // filter settings for each entity field IDs from the data of main list screen
        val filters: List<FilterSettings> = listOf()
    )

    data class FilterSettings(
        val fieldID: EntityFieldID,
        val from: Any? = null,
        val to: Any? = null,
        val fieldsList: List<FilteredField> = listOf(),
        val isActive: Boolean = false
    )

    data class FilteredField(val field: EntityField, val isFiltered: Boolean = false)
}

