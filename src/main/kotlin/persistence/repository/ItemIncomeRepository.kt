package persistence.repository

import com.akhris.domain.core.repository.BaseCachedRepository
import com.akhris.domain.core.repository.ISpecification
import domain.entities.ItemIncome
import persistence.datasources.IItemsIncomeDao

class ItemIncomeRepository(private val incomeDao: IItemsIncomeDao) : BaseCachedRepository<String, ItemIncome>(),
    IItemIncomeRepository {
    override suspend fun fetchItemFromRepo(idToFetch: String): ItemIncome? {
        return incomeDao.getByID(idToFetch)
    }

    override suspend fun insertInRepo(entity: ItemIncome) {
        incomeDao.insert(entity)
    }

    override suspend fun removeFromRepo(entity: ItemIncome) {
        TODO("Not yet implemented")
    }

    override suspend fun updateInRepo(entity: ItemIncome) {
        TODO("Not yet implemented")
    }

    override suspend fun query(specification: ISpecification): List<ItemIncome> {
        TODO("Not yet implemented")
    }

    override suspend fun remove(specification: ISpecification) {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsCount(specification: ISpecification): Long {
        TODO("Not yet implemented")
    }

}