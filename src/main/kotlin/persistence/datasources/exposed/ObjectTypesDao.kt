package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.ObjectType
import org.jetbrains.exposed.sql.*
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
            Tables.ParametersToObjectType.batchInsert(entity.parameters) { p ->
                this[Tables.ParametersToObjectType.objectType] = entity.id.toUUID()
                this[Tables.ParametersToObjectType.parameter] = p.id.toUUID()
            }
            commit()
        }
    }

    override suspend fun update(entity: ObjectType) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)

            //1. get entity by id:
            val objectType = EntityObjectType[UUID.fromString(entity.id)]
            //2. update it:
            objectType.name = entity.name

            //3. if parameters list changed - update it:
            if (entity.parameters.map { it.id } != objectType.parameters.map { it.id.value.toString() }) {
                //remove all old parameters
                Tables
                    .ParametersToObjectType
                    .deleteWhere { Tables.ParametersToObjectType.objectType eq objectType.id }

                //batch insert all new parameters
                Tables.ParametersToObjectType.batchInsert(entity.parameters) { p ->
                    this[Tables.ParametersToObjectType.objectType] = entity.id.toUUID()
                    this[Tables.ParametersToObjectType.parameter] = p.id.toUUID()
                }
            }
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val objectType = EntityObjectType[UUID.fromString(id)]
            objectType.delete()
        }
    }
}