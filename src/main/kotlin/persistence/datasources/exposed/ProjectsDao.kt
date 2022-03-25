package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Project
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.dto.exposed.EntityProject
import persistence.dto.exposed.Tables
import persistence.dto.exposed.Tables.ProjectItems.count
import persistence.dto.exposed.Tables.ProjectItems.item
import persistence.dto.exposed.Tables.ProjectItems.project
import persistence.mappers.toProject
import utils.toUUID
import java.util.*

class ProjectsDao : BaseDao<Project> {
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

    override suspend fun getAll(): List<Project> {
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
}