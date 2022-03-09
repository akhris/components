package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.ItemIncome
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IItemsDao
import persistence.datasources.IItemsIncomeDao
import persistence.dto.exposed.*
import persistence.mappers.toItemIncome
import persistence.mappers.toUnit
import java.util.*

class ItemsIncomeDao(private val itemsDao: IItemsDao) : IItemsIncomeDao {
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

    override suspend fun getAll(): List<ItemIncome> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(entity: ItemIncome) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val item = entity.item?.entity?.id?.let { itemsDao.getByID(it) }?:
            EntityItemIncome.new {
                item = entity.item?.entity?.id?.let { EntityItem[UUID.fromString(it)] }
                count = entity.item?.count
                container = entity.container?.id?.let { EntityContainer[UUID.fromString(it)] }
                dateTime = entity.dateTime
                supplier = entity.supplier?.id?.let { EntitySupplier[UUID.fromString(it)] }
            }
            commit()
        }
    }

    override suspend fun update(entity: ItemIncome) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun query(offset: Int, limit: Int): List<ItemIncome> {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsCount(): Long {
        TODO("Not yet implemented")
    }
}