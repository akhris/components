package persistence.columnMappers

import domain.entities.ObjectType
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ObjectTypeExposedColumnMapper : IDBColumnMapper<ObjectType> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column =  when (fieldID) {
            is EntityFieldID.EntityID -> Tables.ObjectTypes.parent
            is EntityFieldID.EntitiesListID -> null
            is EntityFieldID.StringID -> Tables.ObjectTypes.name
            else -> null
        } as? Column<Any?>
        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}