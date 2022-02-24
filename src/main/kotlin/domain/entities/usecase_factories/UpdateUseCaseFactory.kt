package domain.entities.usecase_factories

import com.akhris.domain.core.application.UpdateEntity
import com.akhris.domain.core.entities.IEntity
import domain.application.*
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass

class UpdateUseCaseFactory(
    private val updateItem: UpdateItem,
    private val updateParameter: UpdateParameter,
    private val updateUnit: UpdateUnit,
    private val updateObjectType: UpdateObjectType,
    private val updateContainer: UpdateContainer,
    private val updateSupplier: UpdateSupplier,
    private val updateItemIncome: UpdateItemIncome,
    private val updateItemOutcome: UpdateItemIncome,
    private val updateProject: UpdateProject,
    private val updateWarehouseItem: UpdateWarehouseItem
) : IUpdateUseCaseFactory {



    override fun <ID, T : IEntity<ID>> getUpdateUseCase(entityClass: KClass<out T>): UpdateEntity<ID, out T> {
        val a = when (entityClass) {
            Item::class -> updateItem
            Parameter::class -> updateParameter
            Unit::class -> updateUnit
            ObjectType::class -> updateObjectType
            Container::class -> updateContainer
            Supplier::class -> updateSupplier
            ItemIncome::class -> updateItemIncome
            ItemOutcome::class -> updateItemOutcome
            Project::class -> updateProject
            WarehouseItem::class -> updateWarehouseItem
            else -> throw IllegalArgumentException("not found update-use-case for entity class: $entityClass")
        }
        return a as UpdateEntity<ID, out T>
    }
}