package persistence.columnMappers

import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class UnitExposedColumnMapper : IDBColumnMapper<domain.entities.Unit> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.StringID -> Tables.Units.unit
            is EntityFieldID.BooleanID -> Tables.Units.isMultipliable
            else -> null
        } as? Column<Any>
    }
}