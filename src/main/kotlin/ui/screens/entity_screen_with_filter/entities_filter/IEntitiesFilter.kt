package ui.screens.entity_screen_with_filter.entities_filter

import com.arkivanov.decompose.value.Value
import domain.entities.fieldsmappers.EntityFieldID
import ui.screens.types_of_data.types_selector.ItemRepresentationType

interface IEntitiesFilter {
    //current state of the screen
    val state: Value<Model>


    //methods to change the state:
    //change representation (cards/ grid):
    fun changeItemRepresentationType(itemRepresentationType: ItemRepresentationType)

    // add / update filter setting
    fun setFilter(filterSettings: FilterSettings)

    // remove filter setting
    fun removeFilter(filterSettings: FilterSettings)

    //filter screen model:
    data class Model(
        //cards / grid
        val itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Table,

        // filter settings for each entity field IDs from the data of main list screen
        val filters: List<FilterSettings> = listOf()
    )

    data class FilterSettings(val fieldID: EntityFieldID, val from: Any?, val to: Any?, val inList: List<Any>)
}