package domain.entities.usecase_factories

import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.entities.IEntity
import domain.application.*
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass

class InsertUseCaseFactory(
    private val insertItem: InsertItem,
    private val insertParameter: InsertParameter,
    private val insertUnit: InsertUnit,
    private val insertObjectType: InsertObjectType,
    private val insertContainer: InsertContainer,
    private val insertSupplier: InsertSupplier,
    private val insertItemIncome: InsertItemIncome,
    private val insertItemOutcome: InsertItemOutcome,
    private val insertProject: InsertProject,
    private val insertInvoice: InsertInvoice
) : IInsertUseCaseFactory {

    override fun <T : IEntity<*>> getInsertUseCase(entityClass: KClass<out T>): InsertEntity<*, T>? {
        val a = when (entityClass) {
            Item::class -> insertItem
            Parameter::class -> insertParameter
            Unit::class -> insertUnit
            ObjectType::class -> insertObjectType
            Container::class -> insertContainer
            Supplier::class -> insertSupplier
            ItemIncome::class -> insertItemIncome
            ItemOutcome::class -> insertItemOutcome
            Project::class -> insertProject
            Invoice::class -> insertInvoice
            else -> null
        }
        return a as? InsertEntity<*, T>
    }
}