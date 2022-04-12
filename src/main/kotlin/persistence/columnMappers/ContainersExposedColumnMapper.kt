package persistence.columnMappers

import domain.entities.Container
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import persistence.dto.exposed.Tables

class ContainersExposedColumnMapper : IDBColumnMapper<Container> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> null
            is EntityFieldID.StringID -> when (fieldID.tag) {
                EntityFieldID.tag_name -> Tables.Containers.name.name
                EntityFieldID.tag_description -> Tables.Containers.description.name
                else -> null
            }
            else -> null
        }
    }
}