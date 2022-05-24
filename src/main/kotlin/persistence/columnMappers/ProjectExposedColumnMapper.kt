package persistence.columnMappers

import domain.entities.Project
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ProjectFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ProjectExposedColumnMapper : IDBColumnMapper<Project> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            ProjectFieldsMapper.tag_ext_file -> Tables.Projects.extFile
            EntityFieldID.tag_name -> Tables.Projects.name
            EntityFieldID.tag_description -> Tables.Projects.description
            EntityFieldID.tag_date_time -> Tables.Projects.dateTime
            else -> null
        } as? Column<Any?>

        return column?.let { IDBColumnMapper.Result(column = column) }

    }
}