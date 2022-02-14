package ui.screens.entity_screen_with_filter.entities_filter

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import domain.entities.fieldsmappers.IFieldsMapper
import ui.screens.entity_screen_with_filter.entities_list.IEntitiesList
import ui.screens.types_of_data.types_selector.ItemRepresentationType
import utils.replace

class EntitiesFilterComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    listModel: Value<IEntitiesList.Model<T>>,
    private val onFilterModelChanged: (IEntitiesFilter.Model) -> Unit,
    private val mapper: IFieldsMapper<T>
) : IEntitiesFilter, ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesFilter.Model())

    override val state: Value<IEntitiesFilter.Model> = _state

    override fun changeItemRepresentationType(itemRepresentationType: ItemRepresentationType) {
        _state.reduce {
            it.copy(itemRepresentationType = itemRepresentationType)
        }
    }

    override fun setFilter(filterSettings: IEntitiesFilter.FilterSettings) {
        _state.reduce { model ->
            val isFilterPresented = model.filters.find { it.fieldID == filterSettings.fieldID } != null

            model.copy(
                filters = if (isFilterPresented) {
                    model.filters.replace(filterSettings) { fs ->
                        fs.fieldID == filterSettings.fieldID
                    }
                } else {
                    model.filters.plus(filterSettings)
                }
            )
        }
        onFilterModelChanged(_state.value)
    }

    override fun removeFilter(filterSettings: IEntitiesFilter.FilterSettings) {
        _state.reduce { model ->
            val filterPresented = model.filters.find { it.fieldID == filterSettings.fieldID }
            model.copy(
                filters = filterPresented?.let {
                    model.filters.minus(it)
                } ?: model.filters
            )
        }
        onFilterModelChanged(_state.value)
    }

    init {
        listModel.subscribe {
            //get set of field id's here and update _state if necessary
            val fieldIDs = it.entities.flatMap { e -> mapper.getEntityIDs(e) }.toSet()

        }
    }
}