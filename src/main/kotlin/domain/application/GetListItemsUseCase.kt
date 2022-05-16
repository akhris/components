package domain.application

import com.akhris.domain.core.application.UseCase
import com.akhris.domain.core.di.IoDispatcher
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import kotlinx.coroutines.CoroutineDispatcher
import persistence.datasources.EntitiesList
import persistence.repository.IGroupingRepository

class GetListItemsUseCase<ID, ENTITY : IEntity<ID>>(
    val repo: IRepository<ID, ENTITY>,
    @IoDispatcher
    ioDispatcher: CoroutineDispatcher
) : UseCase<EntitiesList<ENTITY>, GetListItemsUseCase.Params>(ioDispatcher) {

    override suspend fun run(params: Params): EntitiesList<ENTITY> {
        return when (params) {
            is Params.GetWithSpecification -> getEntitiesWithSpec(params.specification)
        }
    }

    private suspend fun getEntitiesWithSpec(specification: ISpecification): EntitiesList<ENTITY> {
        val gRepo = (repo as? IGroupingRepository<ID, ENTITY>)
        if (gRepo == null)
            log("$repo does not implement IGroupingRepository, so not grouping is allowed")

        return gRepo?.gQuery(specification) ?: EntitiesList.NotGrouped(repo.query(specification))
    }


    sealed class Params {

        class GetWithSpecification(val specification: ISpecification) : Params()
    }

}

