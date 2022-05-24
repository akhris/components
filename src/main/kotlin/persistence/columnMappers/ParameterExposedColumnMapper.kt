package persistence.columnMappers

import domain.entities.Parameter
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ParameterFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ParameterExposedColumnMapper : IDBColumnMapper<Parameter> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            ParameterFieldsMapper.tag_unit -> Tables.Parameters.unit
            EntityFieldID.tag_name -> Tables.Parameters.name
            EntityFieldID.tag_description -> Tables.Parameters.description
            else -> null
        } as? Column<Any?>

        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}