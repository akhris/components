package domain.entities.usecase_factories

import com.akhris.domain.core.application.*
import com.akhris.domain.core.entities.IEntity
import kotlin.reflect.KClass

interface IGetUseCaseFactory {
    fun <ID, T : IEntity<ID>> getUseCase(entityClass: KClass<out T>): GetEntity<ID, out T>
}

interface IGetListUseCaseFactory {
    fun <T : IEntity<*>> getListUseCase(entityClass: KClass<out T>): GetEntities<*, out T>
}

interface IUpdateUseCaseFactory {
    fun <T : IEntity<*>> getUpdateUseCase(entityClass: KClass<out T>): UpdateEntity<*, out T>
}

interface IInsertUseCaseFactory {
    fun <T : IEntity<*>> getInsertUseCase(entityClass: KClass<out T>): InsertEntity<*, T>
}

interface IRemoveUseCaseFactory {
    fun <T : IEntity<*>> getRemoveUseCase(entityClass: KClass<out T>): RemoveEntity<*, T>?
}