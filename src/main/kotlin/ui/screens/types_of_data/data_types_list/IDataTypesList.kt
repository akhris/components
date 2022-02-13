package ui.screens.types_of_data.data_types_list

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.value.Value
import ui.screens.types_of_data.types_selector.ITypesSelector

interface IDataTypesList {
    val state: Value<State>

    fun onEntityRemoved(entity: IEntity<*>)

    data class State(
        val selectedType: ITypesSelector.Type,
        val entities: List<IEntity<*>>,
        val itemRepresentationType: ITypesSelector.ItemRepresentationType
    )
}