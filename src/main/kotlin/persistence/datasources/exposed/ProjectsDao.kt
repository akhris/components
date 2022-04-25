package persistence.datasources.exposed

import domain.entities.Project
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityProject
import persistence.dto.exposed.Tables
import persistence.mappers.toProject
import utils.toUUID

class ProjectsDao(columnMappersFactory: ColumnMappersFactory) : BaseUUIDDao<Project, EntityProject, Tables.Projects>(
    table = Tables.Projects,
    entityClass = EntityProject,
    columnMapper = columnMappersFactory.getColumnMapper(Project::class)
) {
    override fun mapToEntity(exposedEntity: EntityProject): Project = exposedEntity.toProject()

    override fun insertStatement(entity: Project): Tables.Projects.(InsertStatement<Number>) -> Unit = {
        it[name] = entity.name
        it[description] = entity.description
        it[dateTime] = entity.dateTime
        it[extFile] = entity.extFile
    }

    override fun Transaction.doAfterInsert(entity: Project) {
        Tables.ProjectItems.batchInsert(entity.items) { i ->
            this[Tables.ProjectItems.project] = entity.id.toUUID()
            this[Tables.ProjectItems.item] = i.entity.id.toUUID()
            this[Tables.ProjectItems.count] = i.count
        }
    }

    override fun updateStatement(entity: Project): Tables.Projects.(UpdateStatement) -> Unit = {
        it[name] = entity.name
        it[description] = entity.description
        it[dateTime] = entity.dateTime
        it[extFile] = entity.extFile
    }

    override fun Transaction.doAfterUpdate(entity: Project) {
        //3. if items list changed - update it:
        //remove all old parameters
        Tables
            .ProjectItems
            .deleteWhere { Tables.ProjectItems.project eq entity.id.toUUID() }

        //batch insert all new parameters
        Tables
            .ProjectItems
            .batchInsert(entity.items) { i ->
                this[Tables.ProjectItems.project] = entity.id.toUUID()
                this[Tables.ProjectItems.item] = i.entity.id.toUUID()
                this[Tables.ProjectItems.count] = i.count
            }

    }

}


/*
    override suspend fun getByID(id: String): Project? {
        return try {
            newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityProject.get(id = UUID.fromString(id)).toProject()
            }
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    override suspend fun getAll(filters: List<FilterSpec>): List<Project> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityProject.all().map { it.toProject() }
        }
    }

    override suspend fun insert(entity: Project) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Projects.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                statement[description] = entity.description
            }
            Tables.ProjectItems.batchInsert(entity.items) { i ->
                this[project] = entity.id.toUUID()
                this[item] = i.entity.id.toUUID()
                this[count] = i.count
            }
            commit()
        }
    }

    override suspend fun update(entity: Project) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val project = EntityProject[entity.id.toUUID()]

            //2. update it:
            project.name = entity.name
            project.description = entity.description
            //3. if items list changed - update it:
            if (entity.items.map { it.entity.id to it.count } != project.items.map { it.id.value.toString() to it.count }) {
                log("project items changed, updating...")
                //remove all old parameters
                Tables
                    .ProjectItems
                    .deleteWhere { Tables.ProjectItems.project eq entity.id.toUUID() }

                //batch insert all new parameters
                Tables
                    .ProjectItems
                    .batchInsert(entity.items) { i ->
                        this[Tables.ProjectItems.project] = entity.id.toUUID()
                        this[Tables.ProjectItems.item] = i.entity.id.toUUID()
                        this[Tables.ProjectItems.count] = i.count
                    }
            }
            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val project = EntityProject[id.toUUID()]
            project.delete()
            commit()
        }
    }

     */