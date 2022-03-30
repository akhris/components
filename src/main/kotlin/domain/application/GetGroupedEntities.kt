package domain.application

import com.akhris.domain.core.application.UseCase
import com.akhris.domain.core.di.IoDispatcher
import com.akhris.domain.core.entities.IEntity
import kotlinx.coroutines.CoroutineDispatcher
import persistence.repository.IGSFPRepository
import persistence.repository.Specification

class GetGroupedEntities<ID, ENTITY : IEntity<ID>>(
    private val repo: IGSFPRepository<ENTITY>,
    @IoDispatcher
    ioDispatcher: CoroutineDispatcher
) : UseCase<List<IGSFPRepository.GroupedResult<ENTITY>>, GetGroupedEntities.Params>(ioDispatcher) {


    override suspend fun run(params: Params): List<IGSFPRepository.GroupedResult<ENTITY>> {
        return when (params) {
            is Params.GetData -> {
                val groupingSpec = params.specifications.find { it is Specification.Grouped }
                val filterSpec = params.specifications.find { it is Specification.Filters }
                val sortingSpec = params.specifications.find { it is Specification.Sorted }
                val pagingSpec = params.specifications.find { it is Specification.Paginated }
                repo.query(
                    groupingSpec = groupingSpec,
                    filterSpec = filterSpec,
                    sortingSpec = sortingSpec,
                    pagingSpec = pagingSpec
                )
            }
        }
    }

    sealed class Params {
        class GetData(val specifications: List<Specification>) : Params()
    }
}