package persistence.columnMappers

import domain.entities.ObjectType
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import persistence.dto.exposed.Tables

class ObjectTypeExposedColumnMapper : IDBColumnMapper<ObjectType> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> null
            is EntityFieldID.EntitiesListID -> null
            is EntityFieldID.StringID -> Tables.ObjectTypes.name.name
            else -> null
        }
    }
}