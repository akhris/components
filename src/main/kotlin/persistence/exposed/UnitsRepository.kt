package persistence.exposed

import com.akhris.domain.core.repository.BaseCachedRepository
import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import domain.entities.Unit
import domain.repository.IUnitsRepository
import persistence.dao.IUnitsDao
import persistence.repository.Specification

class UnitsRepository(private val unitsDao: IUnitsDao) : BaseCachedRepository<String, Unit>(), IUnitsRepository {
    override suspend fun fetchItemFromRepo(idToFetch: String): Unit? {
        return unitsDao.getByID(idToFetch)
    }

    override suspend fun insertInRepo(entity: Unit) {
        unitsDao.insert(entity)
    }

    override suspend fun removeFromRepo(entity: Unit) {
        unitsDao.removeById(entity.id)
    }

    override suspend fun updateInRepo(entity: Unit) {
        unitsDao.update(entity)
    }

    override suspend fun query(specification: ISpecification): List<Unit> {
        log("query: $specification")
        if (specification !is Specification) {
            throw IllegalArgumentException("unknown specification: $specification")
        }

        return when (specification) {
            is Specification.ByItem -> {
                listOf()
            }
            Specification.QueryAll -> {
                unitsDao.getAll()
            }
            is Specification.Search -> {
                listOf()
            }
        }
    }

    override suspend fun remove(specification: ISpecification) {
        if (specification !is Specification) {
            throw IllegalArgumentException("unknown specification: $specification")
        }

    }

    override suspend fun getItemsCount(specification: ISpecification): Long {
        return unitsDao.getItemsCount()
    }

}