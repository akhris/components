package ui.screens.entities_screen.entities_grouping

import com.arkivanov.decompose.value.Value
import domain.entities.fieldsmappers.EntityFieldID

interface IEntitiesGrouping {

    val state: Value<Model>

    fun groupBy(fieldID: EntityFieldID)

    data class Model(
        val groupingSettings: List<GroupingSettings> = listOf(),
        val currentGrouping: GroupingSettings? = null
    )

    data class GroupingSettings(
        val fieldID: EntityFieldID
    )

}