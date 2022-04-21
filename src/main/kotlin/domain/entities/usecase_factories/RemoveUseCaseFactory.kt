package domain.entities.usecase_factories

import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.entities.IEntity
import domain.application.*
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass

class RemoveUseCaseFactory(
    private val removeItem: RemoveItem,
    private val removeParameter: RemoveParameter,
    private val removeUnit: RemoveUnit,
    private val removeObjectType: RemoveObjectType,
    private val removeContainer: RemoveContainer,
    private val removeSupplier: RemoveSupplier,
    private val removeItemIncome: RemoveItemIncome,
    private val removeItemOutcome: RemoveItemOutcome,
    private val removeProject: RemoveProject,
    private val removeInvoice: RemoveInvoice
) : IRemoveUseCaseFactory {

    override fun <T : IEntity<*>> getRemoveUseCase(entityClass: KClass<out T>): RemoveEntity<*, T>? {
        val a = when (entityClass) {
            Item::class -> removeItem
            Parameter::class -> removeParameter
            Unit::class -> removeUnit
            ObjectType::class -> removeObjectType
            Container::class -> removeContainer
            Supplier::class -> removeSupplier
            ItemIncome::class -> removeItemIncome
            ItemOutcome::class -> removeItemOutcome
            Project::class -> removeProject
            Invoice::class -> removeInvoice
            else -> null
        }
        return a as? RemoveEntity<*, T>
    }
}