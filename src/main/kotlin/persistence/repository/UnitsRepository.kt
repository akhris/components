package persistence.repository

import com.akhris.domain.core.mappers.Mapper
import com.akhris.domain.core.repository.BaseCachedRepository
import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import domain.entities.Unit
import persistence.datasources.IUnitsDao
import persistence.dto.exposed.EntityUnit

class UnitsRepository(private val unitsDao: IUnitsDao, private val mapper: Mapper<Unit, EntityUnit>) :
    BaseCachedRepository<String, Unit>(), IUnitsRepository {

    override suspend fun fetchItemFromRepo(idToFetch: String): Unit? {
        return unitsDao.getByID(idToFetch)?.let { mapper.mapFrom(it) }
    }

    override suspend fun insertInRepo(entity: Unit) {
        unitsDao.insert(mapper.mapTo(entity))
    }

    override suspend fun removeFromRepo(entity: Unit) {
        unitsDao.removeById(entity.id)
    }

    override suspend fun updateInRepo(entity: Unit) {
        unitsDao.update(mapper.mapTo(entity))
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
                mapper.mapFrom(unitsDao.getAll()).toList()
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
//
//    override suspend fun getItemsCount(specification: ISpecification): Long {
//        return unitsDao.getItemsCount()
//    }

}