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
        incomeDao.removeById(entity.id)
    }

    override suspend fun updateInRepo(entity: ItemIncome) {
        incomeDao.update(entity)
    }

    override suspend fun query(specification: ISpecification): List<ItemIncome> {
        if (specification !is Specification) {
            throw IllegalArgumentException("unknown specification: $specification")
        }
        return when (specification) {
            is Specification.ByItem -> TODO()
            Specification.QueryAll -> incomeDao.getAll()
            is Specification.Search -> TODO()
            is Specification.Paginated -> incomeDao.query(
                offset = specification.itemsPerPage * specification.pageNumber,
                limit = specification.pageNumber
            )
        }
    }

    override suspend fun remove(specification: ISpecification) {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsCount(specification: ISpecification): Long {
        return incomeDao.getItemsCount()
    }

}