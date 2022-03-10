package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Item
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.dto.exposed.EntityItem
import persistence.dto.exposed.Tables
import persistence.mappers.toItem
import utils.toUUID
import java.util.*

class ItemsDao : BaseDao<Item> {
    override suspend fun getByID(id: String): Item? {
        return try {
            newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityItem.get(id = UUID.fromString(id)).toItem()
            }
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    override suspend fun getAll(): List<Item> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityItem.all().map { it.toItem() }
        }
    }

    override suspend fun insert(entity: Item) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Items.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                entity.type?.id?.toUUID()?.let {
                    statement[type] = it
                }

            }
            commit()
        }
    }

    override suspend fun update(entity: Item) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: String) {
        TODO("Not yet implemented")
    }
}