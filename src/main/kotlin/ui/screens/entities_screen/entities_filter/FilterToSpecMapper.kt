package ui.screens.entities_screen.entities_filter

import com.akhris.domain.core.entities.IEntity
import persistence.repository.FilterSpec
import persistence.repository.Specification
import kotlin.reflect.KClass

fun List<IEntitiesFilter.FilterSettings>.toSpec(entityClass: KClass<out IEntity<*>>?): Specification.Filtered {
    val filterSpecs = if (entityClass == null || isEmpty()) listOf() else mapNotNull { filterSettings ->

        val filteredValuesForFieldID = filterSettings.fieldsList.filter { it.isFiltered }.map { it.field }
        if (filteredValuesForFieldID.isEmpty()) {
            null
        } else
            FilterSpec(
                entityClass = entityClass,
                fieldID = filterSettings.fieldID,
                filteredValues = filteredValuesForFieldID
            )
    }

    return Specification.Filtered(filterSpecs)
}