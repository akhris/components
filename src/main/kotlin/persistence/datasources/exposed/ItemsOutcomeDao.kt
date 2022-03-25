package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.ItemOutcome
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import persistence.datasources.IItemsOutcomeDao
import persistence.dto.exposed.EntityItemOutcome
import persistence.dto.exposed.Tables
import persistence.mappers.toItemOutcome
import utils.set
import utils.toUUID
import java.util.*

class ItemsOutcomeDao : IItemsOutcomeDao {
    override suspend fun getByID(id: String): ItemOutcome? {
        return try {
            newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityItemOutcome.get(id = UUID.fromString(id)).toItemOutcome()
            }
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    override suspend fun insert(entity: ItemOutcome) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
//            val item = entity.item?.entity?.id?.let { itemsDao.getByID(it) }
            Tables.ItemOutcomes.insert { statement ->
                statement[id] = entity.id.toUUID()
                entity.item?.entity?.id?.toUUID()?.let {
                    statement[item] = it
                }
                statement[count] = entity.item?.count
                entity.container?.id?.toUUID()?.let {
                    statement[container] = it
                }
                statement[dateTime] = entity.dateTime
            }

            commit()
        }
    }

    override suspend fun update(entity: ItemOutcome) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables
                .ItemOutcomes
                .update({ Tables.ItemOutcomes.id eq entity.id.toUUID() })  { statement ->
                    statement[Tables.ItemOutcomes.dateTime] = entity.dateTime
                    statement[Tables.ItemOutcomes.container] = entity.container?.id?.toUUID()
                    statement[Tables.ItemOutcomes.item] = entity.item?.entity?.id?.toUUID()
                    statement[Tables.ItemOutcomes.count] = entity.item?.count
                }
            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val entity = EntityItemOutcome[id.toUUID()]
            entity.delete()
            commit()
        }
    }

    override suspend fun getAll(): List<ItemOutcome> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityItemOutcome.all().map { it.toItemOutcome() }
        }
    }

    override suspend fun query(offset: Long, limit: Long): List<ItemOutcome> {
        log("query. offset: $offset limit: $limit")
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityItemOutcome
                .all()
                .limit(n = limit.toInt(), offset = offset)
                .map { it.toItemOutcome() }
        }
    }

    override suspend fun getItemsCount(): Long {
        return newSuspendedTransaction {
            EntityItemOutcome.all().count()
        }
    }
}