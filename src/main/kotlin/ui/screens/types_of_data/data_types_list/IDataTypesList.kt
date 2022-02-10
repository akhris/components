package ui.screens.types_of_data.data_types_list

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.value.Value

interface IDataTypesList {
    val state: Value<State>

    fun onEntityRemoved(entity: IEntity<*>)

    data class State(val entities: List<IEntity<*>>)
}