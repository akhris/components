package persistence.columnMappers

import domain.entities.Project
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import persistence.dto.exposed.Tables

class ProjectExposedColumnMapper : IDBColumnMapper<Project> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> null
            is EntityFieldID.EntitiesListID -> null
            is EntityFieldID.StringID ->
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> Tables.Projects.name.name
                    EntityFieldID.tag_description -> Tables.Projects.description.name
                    else -> null
                }

            else -> null
        }
    }
}