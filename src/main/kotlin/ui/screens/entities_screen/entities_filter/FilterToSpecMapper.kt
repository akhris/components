package ui.screens.entities_screen.entities_filter

import com.akhris.domain.core.entities.IEntity
import persistence.repository.FilterSpec
import persistence.repository.Specification
import kotlin.reflect.KClass

fun List<IEntitiesFilter.Filter>.toSpec(entityClass: KClass<out IEntity<*>>?): Specification.Filtered {
    val filterSpecs = if (entityClass == null || isEmpty()) listOf() else mapNotNull { filterSettings ->
        when (filterSettings) {
            is IEntitiesFilter.Filter.Range -> FilterSpec.Range(
                fromValue = filterSettings.from,
                toValue = filterSettings.to,
                entityClass = entityClass,
                fieldID = filterSettings.fieldID
            )
            is IEntitiesFilter.Filter.Values -> FilterSpec.Values(
                filteredValues = filterSettings.fieldsList.mapNotNull { if (it.isFiltered) it.value else null },
                entityClass = entityClass,
                fieldID = filterSettings.fieldID
            )
        }

//        val filteredValuesForFieldID = filterSettings.fieldsList.filter { it.isFiltered }.map { it.field }
//        if (filteredValuesForFieldID.isEmpty()) {
//            null
//        } else
//            FilterSpec(
//                entityClass = entityClass,
//                fieldID = filterSettings.fieldID,
//                filteredValues = filteredValuesForFieldID
//            )
    }

    return Specification.Filtered(filterSpecs)
}