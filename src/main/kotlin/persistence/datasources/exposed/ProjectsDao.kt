package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Project
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
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
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: String) {
        TODO("Not yet implemented")
    }
}