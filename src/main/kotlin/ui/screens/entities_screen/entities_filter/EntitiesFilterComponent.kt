package ui.screens.entities_screen.entities_filter

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import domain.entities.fieldsmappers.FieldsMapperFactory
import utils.replace

class EntitiesFilterComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    private val entities: List<T>,
    private val mapperFactory: FieldsMapperFactory,
    private val onFiltersChange: (List<IEntitiesFilter.FilterSettings>) -> Unit
) : IEntitiesFilter, ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesFilter.Model())

    override val state: Value<IEntitiesFilter.Model> = _state

    override fun setFilter(filterSettings: IEntitiesFilter.FilterSettings) {
        _state.reduce { model ->


//            val isFilterPresented = model.filters.find { it.fieldID == filterSettings.fieldID } != null

            model.copy(
                filters =
                model.filters.replace(filterSettings.copy(isActive = true)) { fs ->
                    fs.fieldID == filterSettings.fieldID
                }

            )
        }
        onFiltersChange(_state.value.filters)
//        onFilterModelChanged(_state.value)
    }

    override fun removeFilter(filterSettings: IEntitiesFilter.FilterSettings) {
        _state.reduce { model ->
            val filterPresented =
                model.filters.find { it.fieldID == filterSettings.fieldID } ?: IEntitiesFilter.FilterSettings(
                    filterSettings.fieldID
                )

            model.copy(
                filters = model.filters.replace(filterPresented.copy(isActive = false)) { fs ->
                    fs.fieldID == filterSettings.fieldID
                }
            )
        }
//        onFilterModelChanged(_state.value)
    }

    private fun updateFilters() {
        log("updating Filters for entities list of size: ${entities.size}")
        val mapper = entities.firstOrNull()?.let { it::class }?.let { mapperFactory.getFieldsMapper(it) } ?: return
        val fieldIDs = entities.flatMap { e -> mapper.getEntityIDs(e) }.toSet()
        log("fieldIDs: $fieldIDs")
        _state.reduce { m ->
            m.copy(filters = fieldIDs.map { fId ->
                val fields =
                    entities.mapNotNull { e -> mapper.getFieldByID(e, fId) }.toSet()
                IEntitiesFilter.FilterSettings(
                    fieldID = fId,
                    fieldsList = fields.map { f -> IEntitiesFilter.FilteredField(field = f, isFiltered = false) })
            })
        }
    }

    init {
        log("initialize filter component for entities: $entities")
        updateFilters()
    }
}