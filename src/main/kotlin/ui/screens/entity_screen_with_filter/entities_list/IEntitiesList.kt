package ui.screens.entity_screen_with_filter.entities_list

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.value.Value
import ui.screens.types_of_data.types_selector.ItemRepresentationType

interface IEntitiesList<T : IEntity<*>> {
    //current state of the screen
    val state: Value<Model<T>>

    //methods to change the state:
    //set entities list:
    fun setEntitiesList(entities: List<T>)

    //entities list model
    data class Model<T : IEntity<*>>(
        val entities: List<T> = listOf(),
        val itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Table
    )
}