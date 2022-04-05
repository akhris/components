package persistence.datasources.exposed

import domain.entities.ItemOutcome
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.dto.exposed.EntityItemOutcome
import persistence.dto.exposed.Tables
import persistence.mappers.toItemOutcome
import utils.set
import utils.toUUID

class ItemsOutcomeDao : BaseUUIDDao<ItemOutcome, EntityItemOutcome, Tables.ItemOutcomes>(
    table = Tables.ItemOutcomes,
    entityClass = EntityItemOutcome
) {
    override fun mapToEntity(exposedEntity: EntityItemOutcome): ItemOutcome = exposedEntity.toItemOutcome()

    override fun insertStatement(entity: ItemOutcome): Tables.ItemOutcomes.(InsertStatement<Number>) -> Unit = {
        it[id] = entity.id.toUUID()
        it[item] = entity.item?.entity?.id?.toUUID()
        it[count] = entity.item?.count
        it[container]=entity.container?.id?.toUUID()
        it[dateTime] = entity.dateTime
    }

    override fun updateStatement(entity: ItemOutcome): Tables.ItemOutcomes.(UpdateStatement) -> Unit = {
        it[dateTime] = entity.dateTime
        it[container] = entity.container?.id?.toUUID()
        it[item] = entity.item?.entity?.id?.toUUID()
        it[count] = entity.item?.count
    }

}


/*
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

    override suspend fun getAll(filters: List<FilterSpec>): List<ItemOutcome> {
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


     */