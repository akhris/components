package ui.screens.entities_screen.entities_filter

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import domain.entities.fieldsmappers.FieldsMapperFactory
import kotlinx.coroutines.*
import persistence.columnMappers.ColumnMappersFactory
import persistence.datasources.SliceValue
import persistence.repository.ISlicingRepository
import utils.replace
import kotlin.reflect.KClass

class EntitiesFilterComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    entityClass: KClass<out T>?,
    private val getEntities: GetEntities<*, out T>?,
    fieldsMapperFactory: FieldsMapperFactory,
    columnMapperFactory: ColumnMappersFactory,
    private val onFiltersChange: (List<IEntitiesFilter.Filter>) -> Unit
) : IEntitiesFilter, ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesFilter.Model())

    override val state: Value<IEntitiesFilter.Model> = _state

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val columnMapper = entityClass?.let { columnMapperFactory.getColumnMapper(it) }
    private val fieldsMapper = entityClass?.let { fieldsMapperFactory.getFieldsMapper(it) }

    override fun setFilter(filter: IEntitiesFilter.Filter) {
        _state.reduce { model ->
            model.copy(
                filters = model.filters.replace(
                    when (filter) {
                        is IEntitiesFilter.Filter.Range -> filter.copy(isActive = true)
                        is IEntitiesFilter.Filter.Values -> filter.copy(isActive = true)
                    }

                ) { fs ->
                    fs.fieldID == filter.fieldID
                }

            )
        }
        onFiltersChange(_state.value.filters)
        scope.launch {
            updateFilters()
        }
    }

    override fun clearFilters() {
        _state.reduce {
            it.copy(filters = it.filters.map { filter ->
                when (filter) {
                    is IEntitiesFilter.Filter.Range -> filter.copy(from = null, to = null)
                    is IEntitiesFilter.Filter.Values -> {
                        val checksOff = filter.fieldsList.map { fv -> fv.copy(isFiltered = false) }
                        filter.copy(fieldsList = checksOff)
                    }
                }
            })
        }
        onFiltersChange(_state.value.filters)
    }

    private suspend fun updateFilters() {
        //0. for updating filters we need ISlicingRepository
        val repo = (getEntities?.repo as? ISlicingRepository) ?: return

        //1. get field ids of given entity:

        val fieldIDs = fieldsMapper?.getEntityIDs().orEmpty()


        val filters = fieldIDs.mapNotNull { fieldID ->

            val existedOtherSlices =
                _state
                    .value
                    .filters
                    .filter { it.fieldID != fieldID }
                    .flatMap { f ->
                        when (f) {
                            is IEntitiesFilter.Filter.Range -> TODO()
                            is IEntitiesFilter.Filter.Values -> f.fieldsList.filter { it.isFiltered }.map { it.value }
                        }
                    }
                        as? List<SliceValue<Any>>


            val columnName = columnMapper?.getColumn(fieldID)
            columnName?.let { cn ->
                val columnValues = repo.getSlice(cn.name, otherSlices = existedOtherSlices ?: listOf())

                IEntitiesFilter.Filter.Values(
                    fieldID,
                    fieldsList = columnValues.map { sv ->
                        val wasFiltered =
                            (_state
                                .value
                                .filters
                                .find { it.fieldID == fieldID } as? IEntitiesFilter.Filter.Values)?.fieldsList?.find { it.value == sv }?.isFiltered
                                ?: false
                        IEntitiesFilter.FilteringValue(sv, wasFiltered)
                    }
                )
            }
        }

        _state.reduce { m ->
            m.copy(filters = filters)
        }
    }

    init {

        lifecycle.subscribe(onDestroy = {
            scope.coroutineContext.cancelChildren()
        })


        scope.launch {
            updateFilters()
        }
    }
}