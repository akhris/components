package domain.entities.usecase_factories

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.entities.IEntity
import domain.application.*
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass

class GetListUseCaseFactory(
    private val getItemsList: GetItemsList,
    private val getParametersList: GetParametersList,
    private val getUnits: GetUnits,
    private val getObjectTypes: GetObjectTypes,
    private val getContainersList: GetContainersList
) :
    IGetListUseCaseFactory {

    override fun <ID, T : IEntity<ID>> getListUseCase(entityClass: KClass<out T>): GetEntities<ID, out T> {
        val a = when (entityClass) {
            Item::class -> getItemsList
            Parameter::class -> getParametersList
            Unit::class -> getUnits
            ObjectType::class -> getObjectTypes
            Container::class -> getContainersList
            else -> throw IllegalArgumentException("not found get-list-use-case for entity class: $entityClass")
        }
        return a as GetEntities<ID, out T>
    }

}