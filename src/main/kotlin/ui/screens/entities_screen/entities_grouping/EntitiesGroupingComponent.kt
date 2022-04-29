package ui.screens.entities_screen.entities_grouping

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.FieldsMapperFactory
import kotlin.reflect.KClass

class EntitiesGroupingComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    entityClass: KClass<out T>?,
    fieldsMapperFactory: FieldsMapperFactory,
    private val onGroupingChange: (EntityFieldID?) -> Unit
) : IEntitiesGrouping, ComponentContext by componentContext {

    private val fieldsMapper = entityClass?.let { fieldsMapperFactory.getFieldsMapper(it) }
//
//    private val scope =
//        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _state = MutableValue(IEntitiesGrouping.Model())

    override val state: Value<IEntitiesGrouping.Model> = _state

    override fun setGrouping(groupFieldID: EntityFieldID?) {
        _state.reduce {
            it.copy(groupedBy = groupFieldID)
        }
        onGroupingChange(groupFieldID)
    }

    init {
//        lifecycle.subscribe(onDestroy = {
//            scope.coroutineContext.cancelChildren()
//        })
//
        updateGroups()

    }

    private fun updateGroups() {
        val groups = fieldsMapper?.getEntityIDs()
        groups?.let { g ->
            _state.reduce { it.copy(fieldsList = g) }
        }
        log("updated groups:")
        log("$groups")
    }
}