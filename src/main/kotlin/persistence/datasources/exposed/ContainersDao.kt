package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Container
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.dto.exposed.EntityContainer
import persistence.dto.exposed.Tables
import persistence.mappers.toContainer
import utils.toUUID
import java.util.*

class ContainersDao : BaseDao<Container> {
    override suspend fun getByID(id: String): Container? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityContainer.get(id = UUID.fromString(id)).toContainer()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(): List<Container> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityContainer.all().map { it.toContainer() }
        }
    }

    override suspend fun insert(entity: Container) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Containers.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                statement[description] = entity.description
            }
            entity.parentContainer?.let { pC ->
                Tables.ContainerToContainers.insert { statement ->
                    statement[parent] = pC.id.toUUID()
                    statement[child] = entity.id.toUUID()
                }
            }
            commit()
        }
    }

    override suspend fun update(entity: Container) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val container = EntityContainer[entity.id.toUUID()]
            //2. update it:
            container.name = entity.name
            container.description = entity.description
            //3. update children-parent reference:
            if (entity.parentContainer == null) {
                //delete reference:
                Tables
                    .ContainerToContainers
                    .deleteWhere { Tables.ContainerToContainers.child eq entity.id.toUUID() }
            } else {
                //update reference
                val rowsUpdated = Tables
                    .ContainerToContainers
                    .update({ Tables.ContainerToContainers.child eq entity.id.toUUID() }) {
                        it[parent] = entity.parentContainer.id.toUUID()
                    }
                //if nothing was updated - insert new row
                if (rowsUpdated == 0) {
                    Tables
                        .ContainerToContainers
                        .insert { statement ->
                            statement[child] = entity.id.toUUID()
                            statement[parent] = entity.parentContainer.id.toUUID()
                        }
                } else {
                    //do nothing
                }
            }


        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val container = EntityContainer[id.toUUID()]
            container.delete()
        }
    }
}