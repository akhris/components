package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Unit
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IUnitsDao
import persistence.dto.exposed.EntityUnit
import persistence.dto.exposed.Tables
import persistence.mappers.toUnit
import utils.toUUID
import java.util.*

class UnitsDao : IUnitsDao {
    override suspend fun getByID(id: String): Unit? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityUnit.get(id = UUID.fromString(id)).toUnit()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(): List<Unit> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityUnit.all().map { it.toUnit() }
        }
    }

    override suspend fun insert(entity: Unit) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Units.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[unit] = entity.unit
                statement[isMultipliable] = entity.isMultipliable
            }
            commit()
        }
    }

    override suspend fun update(entity: Unit) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val foundEntity = EntityUnit[UUID.fromString(entity.id)]
            //2. update it:
            foundEntity.unit = entity.unit
            foundEntity.isMultipliable = entity.isMultipliable
            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val entity = EntityUnit[UUID.fromString(id)]
            //2. delete it
            entity.delete()
            commit()
        }
    }
}