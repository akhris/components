package ui.screens.entities_screen.entities_view_settings

import com.arkivanov.decompose.value.Value

interface IEntitiesViewSettings {
    val state: Value<Model>

    fun onRepresentationTypeChanged(newType: ItemRepresentationType)

    data class Model(
        val representationTypes: List<ItemRepresentationType> = listOf(
            ItemRepresentationType.Card,
            ItemRepresentationType.Table
        ),
        val currentType: ItemRepresentationType = ItemRepresentationType.Card
    )
}