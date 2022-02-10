package domain.entities.usecase_factories

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.entities.IEntity
import kotlin.reflect.KClass

interface IGetUseCaseFactory {
    fun <ID, T : IEntity<ID>> getUseCase(entityClass: KClass<out T>): GetEntity<ID, out T>
}

interface IGetListUseCaseFactory {
    fun <ID, T : IEntity<ID>> getListUseCase(entityClass: KClass<out T>): GetEntities<ID, out T>
}