package persistence.columnMappers

import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import persistence.dto.exposed.Tables

class UnitExposedColumnMapper : IDBColumnMapper<domain.entities.Unit> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.StringID -> Tables.Units.unit.name
            is EntityFieldID.BooleanID -> Tables.Units.isMultipliable.name
            else -> null
        }
    }
}