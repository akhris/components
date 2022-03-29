package persistence.datasources.exposed

import domain.entities.Container
import domain.entities.Item
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables
import persistence.repository.FilterSpec

typealias ExposedFilter<T> = Pair<Column<T>, T>


fun FilterSpec.toExposedFilter(): List<ExposedFilter<Any>> {
    //1. get columns by entity class:
    return when (entityClass) {
        Container::class -> {
            getContainersFilters(fieldID, filteredValues)
        }
        Item::class -> listOf()
        else->listOf()
    }
}

private fun getContainersFilters(
    fieldID: EntityFieldID,
    filteredValues: List<EntityField>
): List<ExposedFilter<Any>> {
    return when (fieldID.tag) {
        //name
        EntityFieldID.tag_name -> filteredValues.mapNotNull {
            (it as? EntityField.StringField)?.value?.let { name ->
                ExposedFilter(Tables.Containers.name, name)
            }
        }
        //description
        EntityFieldID.tag_description -> filteredValues.mapNotNull {
            (it as? EntityField.StringField)?.value?.let { description ->
                ExposedFilter(Tables.Containers.description, description)
            }
        }
        //parent container
        else -> listOf()
    } as List<ExposedFilter<Any>>

}