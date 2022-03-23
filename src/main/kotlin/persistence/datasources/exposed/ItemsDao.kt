package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.Item
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.dto.exposed.EntityItem
import persistence.dto.exposed.EntityObjectType
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

            Tables.ItemValues.batchInsert(entity.values) { v ->
                this[Tables.ItemValues.item] = entity.id.toUUID()
                this[Tables.ItemValues.parameter] = v.entity.id.toUUID()
                this[Tables.ItemValues.value] = v.value
                this[Tables.ItemValues.factor] = v.factor
            }
            commit()
        }
    }

    override suspend fun update(entity: Item) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val item = EntityItem[entity.id.toUUID()]

            //2. update it:
            item.name = entity.name
            item.type = entity.type?.let { EntityObjectType[it.id.toUUID()] }

            //3. update values list:
            //remove all old values
            Tables
                .ItemValues
                .deleteWhere { Tables.ItemValues.item eq entity.id.toUUID() }

            //batch insert all new values
            Tables
                .ItemValues
                .batchInsert(entity.values) { v ->
                    this[Tables.ItemValues.item] = entity.id.toUUID()
                    this[Tables.ItemValues.value] = v.value
                    this[Tables.ItemValues.factor] = v.factor
                    this[Tables.ItemValues.parameter] = v.entity.id.toUUID()
                }
        }
    }

    override suspend fun removeById(id: String) {
        TODO("Not yet implemented")
    }
}