package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IUnitsDao
import persistence.dto.exposed.EntityUnit
import java.util.*

class UnitsDao : IUnitsDao {
    override suspend fun getByID(id: String): EntityUnit? {
        return try {
            newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityUnit.get(id = UUID.fromString(id))
            }
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    override suspend fun getAll(): List<EntityUnit> {
        val all =
            newSuspendedTransaction {
                EntityUnit.all().toList()
            }
        log("got units: ${all.size}")
        return all
    }

    override suspend fun insert(entity: EntityUnit) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
//            Tables.Units.insert { a ->
//                a[unit] = entity.unit
//                a[isMultipliable] = entity.isMultipliable
//            }
            EntityUnit.new {
                unit = entity.unit
                isMultipliable = entity.isMultipliable
            }
            commit()
        }
    }

    override suspend fun update(entity: EntityUnit) {
        log("update: $entity")
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val unit = EntityUnit[entity.id]
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