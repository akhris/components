package ui.screens.entities_screen.entities_grouping

import com.arkivanov.decompose.value.Value
import domain.entities.fieldsmappers.EntityFieldID

interface IEntitiesGrouping {
    //current state of the screen
    val state: Value<Model>

    //methods to change the state:
    // add / update filter setting
    fun setGrouping(groupFieldID: EntityFieldID?)


    //grouping screen model:
    data class Model(
        // the list can be grouped by single EntityFieldID or not grouped if it's null:
        val groupedBy: EntityFieldID? = null,

        // the whole list of fields:
        val fieldsList: List<EntityFieldID> = listOf()
    )

}