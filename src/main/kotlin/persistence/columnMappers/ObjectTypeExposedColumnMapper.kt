package persistence.columnMappers

import domain.entities.ObjectType
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ObjectTypeFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ObjectTypeExposedColumnMapper : IDBColumnMapper<ObjectType> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column =  when (fieldID.tag) {
            ObjectTypeFieldsMapper.tag_parent_type -> Tables.ObjectTypes.parent
            ObjectTypeFieldsMapper.tag_parameters -> null
            EntityFieldID.tag_name -> Tables.ObjectTypes.name
            else -> null
        } as? Column<Any?>
        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}