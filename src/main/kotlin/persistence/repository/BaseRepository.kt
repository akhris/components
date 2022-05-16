package persistence.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.exceptions.NotFoundInRepositoryException
import com.akhris.domain.core.repository.*
import com.akhris.domain.core.utils.log
import kotlinx.coroutines.flow.SharedFlow
import persistence.datasources.EntitiesList
import persistence.datasources.IBaseDao
import persistence.datasources.SliceValue


class BaseRepository<ENTITY : IEntity<String>>(
    private val baseDao: IBaseDao<ENTITY>
) :
    IRepository<String, ENTITY>,
    IRepositoryCallback<ENTITY>,
    IPagingRepository,
    IGroupingRepository<String, ENTITY>,
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

    override suspend fun gQuery(specification: ISpecification): EntitiesList<ENTITY> {
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
                val searchSpec = specification.specs.find { it is Specification.Search } as? Specification.Search
                val groupingSpec = specification.specs.find { it is Specification.Grouped } as? Specification.Grouped
                baseDao.query(
                    filterSpec = filterSpec,
                    sortingSpec = sortingSpec,
                    pagingSpec = pagingSpec,
                    searchSpec = searchSpec,
                    groupingSpec = groupingSpec
                )
            }
            is Specification.Sorted -> baseDao.query(sortingSpec = specification)
            is Specification.Grouped -> baseDao.query(groupingSpec = specification)
        }
    }

    override suspend fun query(specification: ISpecification): List<ENTITY> {
        log("!!! USING DEPRECATED QUERY METHOD !!! with spec: $specification")
        return listOf()
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
                val searchSpec = specification.specs.find { it is Specification.Search } as? Specification.Search
                baseDao.getItemsCount(
                    filterSpec = filterSpec,
                    sortingSpec = sortingSpec,
                    pagingSpec = pagingSpec,
                    searchSpec = searchSpec
                )
            }
            is Specification.Sorted -> baseDao.getItemsCount(sortingSpec = specification)
            is Specification.Grouped -> baseDao.getItemsCount(groupingSpec = specification)
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

    override suspend fun getSlice(columnName: String, otherSlices: List<SliceValue<Any>>): List<SliceValue<*>> {
        return baseDao.slice(columnName, otherSlices)
    }

//    override suspend fun getItemsCount(specification: ISpecification): Long {
//        return requirePagingDao().getItemsCount()
//    }
}