package ui.screens.entities_screen.entities_grouping

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.FieldsMapperFactory

class EntitiesGroupingComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    private val entities: List<T>,
    private val mapperFactory: FieldsMapperFactory
) :
    IEntitiesGrouping, ComponentContext by componentContext {

    private val _state: MutableValue<IEntitiesGrouping.Model> = MutableValue(IEntitiesGrouping.Model())

    override val state: Value<IEntitiesGrouping.Model> = _state


    override fun groupBy(fieldID: EntityFieldID) {
        _state.reduce {
            it.copy(currentGrouping = IEntitiesGrouping.GroupingSettings(fieldID))
        }
    }

    private fun updateFilters() {
        val mapper = entities.firstOrNull()?.let { it::class }?.let { mapperFactory.getFieldsMapper(it) } ?: return
        val fieldIDs = entities.flatMap { e -> mapper.getEntityIDs(e) }.toSet()
        _state.reduce { m ->
            m.copy(groupingSettings = fieldIDs.map { fId ->
                val fields =
                    entities.mapNotNull { e -> mapper.getFieldByID(e, fId) }.toSet()
                IEntitiesGrouping.GroupingSettings(fId)
            })
        }
    }

    init {
        updateFilters()
    }

}