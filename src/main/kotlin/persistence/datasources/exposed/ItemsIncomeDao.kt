package persistence.datasources.exposed

import domain.entities.ItemIncome
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.dto.exposed.EntityItemIncome
import persistence.dto.exposed.Tables
import persistence.mappers.toItemIncome
import utils.set
import utils.toUUID


class ItemsIncomeDao : BaseUUIDDao<ItemIncome, EntityItemIncome, Tables.ItemIncomes>(
    table = Tables.ItemIncomes,
    entityClass = EntityItemIncome
) {
    override fun mapToEntity(exposedEntity: EntityItemIncome): ItemIncome = exposedEntity.toItemIncome()

    override fun insertStatement(entity: ItemIncome): Tables.ItemIncomes.(InsertStatement<Number>) -> Unit = {
        it[id] = entity.id.toUUID()
        it[item] = entity.item?.entity?.id?.toUUID()
        it[count] = entity.item?.count
        it[container] = entity.container?.id?.toUUID()
        it[dateTime] = entity.dateTime
        it[supplier] = entity.supplier?.id?.toUUID()
    }

    override fun updateStatement(entity: ItemIncome): Tables.ItemIncomes.(UpdateStatement) -> Unit = {
        it[dateTime] = entity.dateTime
        it[container] = entity.container?.id?.toUUID()
        it[item] = entity.item?.entity?.id?.toUUID()
        it[count] = entity.item?.count
        it[supplier] = entity.supplier?.id?.toUUID()
    }

}


/*
 override suspend fun getByID(id: String): ItemIncome? {
     return try {
         newSuspendedTransaction {
             addLogger(StdOutSqlLogger)
             EntityItemIncome.get(id = UUID.fromString(id)).toItemIncome()
         }
     } catch (e: Exception) {
         log(e)
         null
     }
 }

 override suspend fun insert(entity: ItemIncome) {
     newSuspendedTransaction {
         addLogger(StdOutSqlLogger)
//            val item = entity.item?.entity?.id?.let { itemsDao.getByID(it) }
         Tables.ItemIncomes.insert { statement ->
             statement[id] = entity.id.toUUID()
             entity.item?.entity?.id?.toUUID()?.let {
                 statement[item] = it
             }
             statement[count] = entity.item?.count
             entity.container?.id?.toUUID()?.let {
                 statement[container] = it
             }
             statement[dateTime] = entity.dateTime
             entity.supplier?.id?.toUUID()?.let {
                 statement[supplier] = it
             }
         }
//            EntityItemIncome.new {
//                item = entity.item?.entity?.id?.let { EntityItem[UUID.fromString(it)] }
//                count = entity.item?.count
//                container = entity.container?.id?.let { EntityContainer[UUID.fromString(it)] }
//                dateTime = entity.dateTime
//                supplier = entity.supplier?.id?.let { EntitySupplier[UUID.fromString(it)] }
//            }
         commit()
     }
 }

 override suspend fun update(entity: ItemIncome) {
     newSuspendedTransaction {
         addLogger(StdOutSqlLogger)
         Tables
             .ItemIncomes
             .update({ Tables.ItemIncomes.id eq entity.id.toUUID() }) { statement ->
                 statement[Tables.ItemIncomes.dateTime] = entity.dateTime
                 statement[Tables.ItemIncomes.container] = entity.container?.id?.toUUID()
                 statement[Tables.ItemIncomes.item] = entity.item?.entity?.id?.toUUID()
                 statement[Tables.ItemIncomes.count] = entity.item?.count
                 statement[Tables.ItemIncomes.supplier] = entity.supplier?.id?.toUUID()
             }
         commit()
     }
 }

 override suspend fun removeById(id: String) {
     newSuspendedTransaction {
         addLogger(StdOutSqlLogger)
//            val entity = EntityItemIncome[id.toUUID()]
//            entity.delete()
         Tables.ItemIncomes.deleteWhere { Tables.ItemIncomes.id eq id.toUUID() }
         commit()
     }
 }

 override suspend fun getAll(filters: List<FilterSpec>): List<ItemIncome> {
     return newSuspendedTransaction {
         addLogger(StdOutSqlLogger)
         EntityItemIncome.all().map { it.toItemIncome() }
     }
 }

 override suspend fun query(offset: Long, limit: Long): List<ItemIncome> {
     log("query. offset: $offset limit: $limit")
     return newSuspendedTransaction {
         addLogger(StdOutSqlLogger)
         EntityItemIncome
             .all()
             .limit(n = limit.toInt(), offset = offset)
             .map { it.toItemIncome() }
     }
 }

 override suspend fun getItemsCount(): Long {
     return newSuspendedTransaction {
         EntityItemIncome.all().count()
     }
 }

  */