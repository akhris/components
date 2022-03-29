package persistence.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.exceptions.NotFoundInRepositoryException
import com.akhris.domain.core.repository.*
import kotlinx.coroutines.flow.SharedFlow
import persistence.datasources.BaseDao
import persistence.datasources.BasePagingDao

class BaseRepository<ENTITY : IEntity<String>>(
    private val baseDao: BaseDao<ENTITY>,
    private val basePagingDao: BasePagingDao<ENTITY>? = null
) :
    IRepository<String, ENTITY>,
    IRepositoryCallback<ENTITY>,
    IPagingRepository {
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
            is Specification.ByItem -> TODO("querying by Specification.ByItem is not yet implemented")
            is Specification.Paginated -> {
                requirePagingDao().query(
                    offset = specification.itemsPerPage * (specification.pageNumber - 1),
                    limit = specification.itemsPerPage
                )
            }
            Specification.QueryAll -> baseDao.getAll(listOf())
            is Specification.Search -> TODO("querying by Specification.Search is not yet implemented")
            is Specification.Filters -> baseDao.getAll(specification.filters)
        }
    }

    private fun requirePagingDao(): BasePagingDao<ENTITY> {
        return basePagingDao
            ?: throw IllegalArgumentException("to use paging functions please provide instance of BasePagingDao")
    }

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

    override suspend fun getItemsCount(specification: ISpecification): Long {
        return requirePagingDao().getItemsCount()
    }
}