package persistence.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.exceptions.NotFoundInRepositoryException
import com.akhris.domain.core.repository.*
import kotlinx.coroutines.flow.SharedFlow
import persistence.datasources.IBaseDao


class BaseRepository<ENTITY : IEntity<String>>(
    private val baseDao: IBaseDao<ENTITY>
) :
    IRepository<String, ENTITY>,
    IRepositoryCallback<ENTITY>,
    IPagingRepository,
    ISlicingRepository {
    private val repoCallbacks: RepositoryCallbacks<ENTITY> = RepositoryCallbacks()

    override val updates: SharedFlow<RepoResult<ENTITY>> = repoCallbacks.updates

    override suspend fun getByID(id: String): ENTITY {
        return baseDao.getByID(id) ?: throw NotFoundInRepositoryException(
            what = "entity with id: $id",
            repository = this.toString()
        )
    }

    override suspend fun insert(t: ENTITY) {
        baseDao.insert(t)
        repoCallbacks.onItemInserted(t)
    }

    override suspend fun query(specification: ISpecification): List<ENTITY> {
        if (specification !is Specification) {
            throw IllegalArgumentException("unknown specification: $specification")
        }
        return when (specification) {
            is Specification.Paginated -> baseDao.query(pagingSpec = specification)
            Specification.QueryAll -> baseDao.query()
            is Specification.Search -> TODO("querying by Specification.Search is not yet implemented")
            is Specification.Filtered -> baseDao.query(filterSpec = specification)
            is Specification.CombinedSpecification -> {
                val sortingSpec = specification.specs.find { it is Specification.Sorted } as? Specification.Sorted
                val filterSpec = specification.specs.find { it is Specification.Filtered } as? Specification.Filtered
                val pagingSpec = specification.specs.find { it is Specification.Paginated } as? Specification.Paginated
                baseDao.query(filterSpec = filterSpec, sortingSpec = sortingSpec, pagingSpec = pagingSpec)
            }
            is Specification.Sorted -> baseDao.query(sortingSpec = specification)
        }
    }

    override suspend fun getItemsCount(specification: ISpecification): Long {
        if (specification !is Specification) {
            throw IllegalArgumentException("unknown specification: $specification")
        }
        return when (specification) {
            is Specification.Paginated -> baseDao.getItemsCount(pagingSpec = specification)
            Specification.QueryAll -> baseDao.getItemsCount()
            is Specification.Search -> TODO("querying by Specification.Search is not yet implemented")
            is Specification.Filtered -> baseDao.getItemsCount(filterSpec = specification)
            is Specification.CombinedSpecification -> {
                val sortingSpec = specification.specs.find { it is Specification.Sorted } as? Specification.Sorted
                val filterSpec = specification.specs.find { it is Specification.Filtered } as? Specification.Filtered
                val pagingSpec = specification.specs.find { it is Specification.Paginated } as? Specification.Paginated
                baseDao.getItemsCount(filterSpec = filterSpec, sortingSpec = sortingSpec, pagingSpec = pagingSpec)
            }
            is Specification.Sorted -> baseDao.getItemsCount(sortingSpec = specification)
        }
    }

//    private fun requirePagingDao(): BasePagingDao<ENTITY> {
//        return basePagingDao
//            ?: throw IllegalArgumentException("to use paging functions please provide instance of BasePagingDao")
//    }


    override suspend fun remove(t: ENTITY) {
        baseDao.removeById(t.id)
        repoCallbacks.onItemRemoved(t)
    }

    override suspend fun remove(specification: ISpecification) {
        TODO("removing by spec is not yet implemented")
    }

    override suspend fun update(t: ENTITY) {
        baseDao.update(t)
        repoCallbacks.onItemUpdated(t)
    }

    override suspend fun getSlice(columnName: String): List<Any> {
        return baseDao.slice(columnName)
    }

//    override suspend fun getItemsCount(specification: ISpecification): Long {
//        return requirePagingDao().getItemsCount()
//    }
}