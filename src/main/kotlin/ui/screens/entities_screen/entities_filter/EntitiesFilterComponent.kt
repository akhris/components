package ui.screens.entities_screen.entities_filter

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import domain.entities.fieldsmappers.FieldsMapperFactory
import kotlinx.coroutines.*
import persistence.repository.ISlicingRepository
import utils.replace
import kotlin.reflect.KClass

class EntitiesFilterComponent<T : IEntity<*>> constructor(
    componentContext: ComponentContext,
    private val entityClass: KClass<out T>?,
    private val getEntities: GetEntities<*, out T>?,
    private val entities: List<T>,
    private val mapperFactory: FieldsMapperFactory,
    private val onFiltersChange: (List<IEntitiesFilter.Filter>) -> Unit
) : IEntitiesFilter, ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesFilter.Model())

    override val state: Value<IEntitiesFilter.Model> = _state

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun setFilter(filter: IEntitiesFilter.Filter) {
        _state.reduce { model ->


//            val isFilterPresented = model.filters.find { it.fieldID == filterSettings.fieldID } != null

            model.copy(
                filters =
                model.filters.replace(filter.copy(isActive = true)) { fs ->
                    fs.fieldID == filter.fieldID
                }

            )
        }
        onFiltersChange(_state.value.filters)
//        onFilterModelChanged(_state.value)
    }

    override fun removeFilter(filter: IEntitiesFilter.Filter) {
        _state.reduce { model ->
            val filterPresented =
                model.filters.find { it.fieldID == filter.fieldID } ?: IEntitiesFilter.Filter(
                    filter.fieldID
                )

            model.copy(
                filters = model.filters.replace(filterPresented.copy(isActive = false)) { fs ->
                    fs.fieldID == filter.fieldID
                }
            )
        }
//        onFilterModelChanged(_state.value)
    }


    private suspend fun updateFilters() {
        //0. for updating filters we need ISlicingRepository
        val repo = (getEntities?.repo as? ISlicingRepository) ?: return

        //1. get field ids of given entity:
        val mapper = entityClass?.let { mapperFactory.getFieldsMapper(it) } ?: return

        val fieldIDs = mapper.getEntityIDs()


        val filters = fieldIDs.map { fieldID ->
            val columnNames = fieldIDs.mapNotNull { it.columnName }
            val a = columnNames.map {
                val columnValues = repo.getSlice(it)
                IEntitiesFilter.Filter.Values(
                    fieldID,
                    fieldsList = columnValues.map { IEntitiesFilter.FilteringValue(it) }
                )
            }

        }
//        //2. get fieldIDs for entity:
//        val fieldIDs = entities.fir
//
//        //3. for each column get filtered values by column name:
//        val sliceValues = table.columns.map {
//            val valuesInTable = repo.getSlice(it.name)
//            IEntitiesFilter.FilterSettings(
//                fieldID = it,
//                fieldsList = valuesInTable.map { IEntitiesFilter.FilteredField(field = mapper.getFieldByID()) })
//
//        }

//        log("fieldIDs: $fieldIDs")
//        _state.reduce { m ->
//            m.copy(filters = fieldIDs.map { fId ->
//                val fields =
//                    entities.mapNotNull { e -> mapper.getFieldByID(e, fId) }.toSet()
//                IEntitiesFilter.FilterSettings(
//                    fieldID = fId,
//                    fieldsList = fields.map { f -> IEntitiesFilter.FilteredField(field = f, isFiltered = false) })
//            })
//        }
    }

    init {

        lifecycle.subscribe(onDestroy = {
            scope.coroutineContext.cancelChildren()
        })


        log("initialize filter component for entities: $entities")
        scope.launch {
            updateFilters()
        }
    }
}