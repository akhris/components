package persistence.columnMappers

import domain.entities.Parameter
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import persistence.dto.exposed.Tables

class ParameterExposedColumnMapper : IDBColumnMapper<Parameter> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> Tables.Parameters.unit.name
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> Tables.Parameters.name.name
                    EntityFieldID.tag_description -> Tables.Parameters.description.name
                    else -> null
                }
            }
            else -> null
        }
    }
}