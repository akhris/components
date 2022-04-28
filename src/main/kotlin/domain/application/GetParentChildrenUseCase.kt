package domain.application

import com.akhris.domain.core.application.UseCase
import com.akhris.domain.core.di.IoDispatcher
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.ISpecification
import domain.entities.IParentableEntity
import kotlinx.coroutines.CoroutineDispatcher

class GetParentChildrenUseCase<ID, ENTITY : IEntity<ID>>(
    val repo: IRepository<ID, ENTITY>,
    @IoDispatcher
    ioDispatcher: CoroutineDispatcher
) : UseCase<List<EntityWithChildren<ENTITY>>, GetParentChildrenUseCase.Params>(ioDispatcher) {

    override suspend fun run(params: Params): List<EntityWithChildren<ENTITY>> {
        return when (params) {
            is Params.GetWithSpecification -> getEntitiesWithSpec(params.specification)
        }
    }

    private suspend fun getEntitiesWithSpec(specification: ISpecification): List<EntityWithChildren<ENTITY>> {
        val wholeList = repo.query(specification)

//        val parentsFilterSpec = Specification.Filtered(filters = listOf(FilterSpec.Values(
//            filteredValues = listOf(SliceValue())
//        )))

        val topLevel = wholeList.filter { (it as? IParentableEntity<ENTITY>)?.parentEntity == null }

        return topLevel.map {
            EntityWithChildren(it, wholeList.filter { e -> (e as? IParentableEntity<ENTITY>)?.parentEntity == it })
        }
    }



    sealed class Params {

        class GetWithSpecification(val specification: ISpecification) : Params()
    }

}

class EntityWithChildren<ENTITY : IEntity<*>>(val entity: ENTITY, val children: List<ENTITY>)