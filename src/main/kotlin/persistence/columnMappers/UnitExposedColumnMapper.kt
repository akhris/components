package persistence.columnMappers

import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.UnitFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class UnitExposedColumnMapper : IDBColumnMapper<domain.entities.Unit> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            UnitFieldsMapper.tag_unit -> Tables.Units.unit
            UnitFieldsMapper.tag_is_multipliable -> Tables.Units.isMultipliable
            else -> null
        } as? Column<Any?>

        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}