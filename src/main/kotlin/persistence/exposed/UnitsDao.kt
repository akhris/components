package persistence.exposed

import com.akhris.domain.core.mappers.Mapper
import com.akhris.domain.core.utils.log
import domain.entities.Unit
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.dao.IUnitsDao
import java.util.*

class UnitsDao(private val mapper: Mapper<Unit, EntityUnit>) : IUnitsDao {
    override suspend fun getByID(id: String): Unit? {
        return try {
            mapper.mapFrom(newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityUnit.get(id = UUID.fromString(id))
            })
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    override suspend fun getAll(): List<Unit> {
        val all =
            newSuspendedTransaction {
                EntityUnit.all().toList()
            }
        log("got units: ${all.size}")
        return mapper.mapFrom(all).toList()
    }

    override suspend fun insert(entity: Unit) {
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

    override suspend fun update(entity: Unit) {
        log("update: $entity")
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val unit = EntityUnit[mapper.mapTo(entity).id]
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
}