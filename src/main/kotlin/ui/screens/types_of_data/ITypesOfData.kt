package ui.screens.types_of_data

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.types_of_data.data_types_list.IDataTypesList
import ui.screens.types_of_data.types_selector.ITypesSelector

/**
 * Interface for TypesOfData component
 */
interface ITypesOfData {


    val selectedItem: Value<ITypesSelector.Type>

    val listRouterState: Value<RouterState<*, ListChild>>
    val filterRouterState: Value<RouterState<*, FilterChild>>

    sealed class ListChild {
        class List(val component: IDataTypesList) : ListChild()
    }

    sealed class FilterChild {
        data class Filter(val component: ITypesSelector) : FilterChild()
    }


}