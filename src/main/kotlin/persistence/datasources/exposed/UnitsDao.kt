package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Unit
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IUnitsDao
import persistence.dto.exposed.EntityUnit
import persistence.mappers.toUnit
import java.util.*

class UnitsDao : IUnitsDao {
    override suspend fun getByID(id: String): Unit? {
        return try {
            newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityUnit.get(id = UUID.fromString(id)).toUnit()
            }
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    override suspend fun getAll(): List<Unit> {
        val all =
            newSuspendedTransaction {
                EntityUnit.all().toList()
            }.map { it.toUnit() }
        log("got units: ${all.size}")
        return all
    }

    override suspend fun insert(entity: Unit) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityUnit.new {
                unit = entity.unit
                isMultipliable = entity.isMultipliable
            }
            commit()
        }
    }

    override suspend fun update(entity: Unit) {
        log("update: $entity")
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val unit = EntityUnit[UUID.fromString(entity.id)]
            //2. update it:
            unit.unit = entity.unit
            unit.isMultipliable = entity.isMultipliable
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val unit = EntityUnit[UUID.fromString(id)]
            //2. delete it
            unit.delete()
        }
    }

//    override suspend fun getItemsCount(): Long {
//        return 0L
//    }
}