package persistence.columnMappers

import domain.entities.Container
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ContainersExposedColumnMapper : IDBColumnMapper<Container> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> null
            is EntityFieldID.StringID -> when (fieldID.tag) {
                EntityFieldID.tag_name -> Tables.Containers.name
                EntityFieldID.tag_description -> Tables.Containers.description
                else -> null
            }
            else -> null
        } as? Column<Any>
    }
}