package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Parameter
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IParametersDao
import persistence.dto.exposed.EntityParameter
import persistence.dto.exposed.Tables
import persistence.mappers.toParameter
import utils.toUUID
import java.util.*

class ParametersDao : IParametersDao {
    override suspend fun getByID(id: String): Parameter? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityParameter.get(id = UUID.fromString(id)).toParameter()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(): List<Parameter> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityParameter.all().map { it.toParameter() }
        }
    }

    override suspend fun insert(entity: Parameter) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Parameters.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                statement[description] = entity.description
                entity.unit?.id?.toUUID()?.let {
                    statement[unit] = it
                }
            }
            commit()
        }
    }

    override suspend fun update(entity: Parameter) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: String) {
        TODO("Not yet implemented")
    }


}