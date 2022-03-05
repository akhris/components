package persistence.datasources.exposed

import com.akhris.domain.core.utils.log
import domain.entities.ItemIncome
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IItemsIncomeDao
import persistence.dto.exposed.EntityItemIncome
import persistence.dto.exposed.EntityUnit
import persistence.mappers.toUnit
import java.util.*

class ItemsIncomeDao : IItemsIncomeDao {
    override suspend fun getByID(id: String): ItemIncome? {
        return try {
            newSuspendedTransaction {
                addLogger(StdOutSqlLogger)
                EntityItemIncome.get(id = UUID.fromString(id))
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
        TODO("Not yet implemented")
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