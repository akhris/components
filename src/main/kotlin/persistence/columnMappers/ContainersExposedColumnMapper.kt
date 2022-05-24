package persistence.columnMappers

import domain.entities.Container
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ContainersExposedColumnMapper : IDBColumnMapper<Container> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            EntityFieldID.tag_entity_id -> Tables.Containers.parent
            EntityFieldID.tag_name -> Tables.Containers.name
            EntityFieldID.tag_description -> Tables.Containers.description
            else -> null
        } as? Column<Any?>
        return column?.let {
            IDBColumnMapper.Result(column = it)
        }
    }
}