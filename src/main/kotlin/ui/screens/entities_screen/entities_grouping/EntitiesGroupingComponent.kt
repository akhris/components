package ui.screens.entities_screen.entities_grouping

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
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
            it.copy(
                currentGrouping = if (it.currentGrouping?.fieldID == fieldID) null else
                    IEntitiesGrouping.GroupingSettings(fieldID)
            )
        }
    }

    private fun updateGroupings() {
        val mapper = entities.firstOrNull()?.let { mapperFactory.getFieldsMapper(it::class) } ?: return
        val fieldIDs =
            entities.flatMap { e -> mapper.getEntityIDs(e) }.filter { it !is EntityFieldID.EntitiesListID }.toSet()
        log("got fieldIDs for grouping: ")
        fieldIDs.forEach {
            log(it)
        }
        _state.reduce { m ->
            m.copy(groupingSettings = fieldIDs.map { fId ->
                val fields =
                    entities.mapNotNull { e -> mapper.getFieldByID(e, fId) }.toSet()
                IEntitiesGrouping.GroupingSettings(fId)
            })
        }
    }

    init {
        updateGroupings()
    }

}