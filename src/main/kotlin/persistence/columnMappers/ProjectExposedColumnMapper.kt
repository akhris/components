package persistence.columnMappers

import domain.entities.Project
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ProjectExposedColumnMapper : IDBColumnMapper<Project> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> null
            is EntityFieldID.EntitiesListID -> null
            is EntityFieldID.StringID ->
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> Tables.Projects.name
                    EntityFieldID.tag_description -> Tables.Projects.description
                    else -> null
                }

            else -> null
        } as? Column<Any>
    }
}