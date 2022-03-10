package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.ObjectType
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.dto.exposed.EntityObjectType
import persistence.dto.exposed.Tables
import persistence.mappers.toObjectType
import utils.toUUID
import java.util.*

class ObjectTypesDao : BaseDao<ObjectType> {
    override suspend fun getByID(id: String): ObjectType? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityObjectType.get(id = UUID.fromString(id)).toObjectType()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(): List<ObjectType> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityObjectType.all().map { it.toObjectType() }
        }
    }

    override suspend fun insert(entity: ObjectType) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.ObjectTypes.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
            }
            commit()
        }
    }

    override suspend fun update(entity: ObjectType) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: String) {
        TODO("Not yet implemented")
    }
}