package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Supplier
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.dto.exposed.EntitySupplier
import persistence.dto.exposed.Tables
import persistence.mappers.toSupplier
import utils.toUUID
import java.util.*

class SuppliersDao : BaseDao<Supplier> {
    override suspend fun getByID(id: String): Supplier? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntitySupplier.get(id = UUID.fromString(id)).toSupplier()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(): List<Supplier> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntitySupplier.all().map { it.toSupplier() }
        }
    }

    override suspend fun insert(entity: Supplier) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Suppliers.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                statement[url] = entity.url
                statement[description] = entity.description
                statement[isFavorite] = entity.isFavorite
            }
            commit()
        }
    }

    override suspend fun update(entity: Supplier) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val foundEntity = EntitySupplier[UUID.fromString(entity.id)]
            //2. update it:
            foundEntity.name = foundEntity.name
            foundEntity.url = foundEntity.url
            foundEntity.description = foundEntity.description
            foundEntity.isFavorite = foundEntity.isFavorite
            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val entity = EntitySupplier[UUID.fromString(id)]
            //2. delete it
            entity.delete()
            commit()
        }
    }
}