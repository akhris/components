package domain.entities.usecase_factories

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.entities.IEntity
import domain.application.*
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass

class GetUseCaseFactory(private val getItem: GetItem,
                        private val getParameter: GetParameter,
                        private val getUnit: GetUnit,
                        private val getObjectType: GetObjectType,
                        private val getContainer: GetContainer,
                        private val getSupplier: GetSupplier,
                        private val getItemIncome: GetItemIncome,
                        private val getItemOutcome: GetItemOutcome,
                        private val getProject: GetProject) : IGetUseCaseFactory {
    override fun <ID, T : IEntity<ID>> getUseCase(entityClass: KClass<out T>): GetEntity<ID, out T> {
        val a = when (entityClass) {
            Item::class -> getItem
            Parameter::class -> getParameter
            Unit::class -> getUnit
            ObjectType::class -> getObjectType
            Container::class -> getContainer
            Supplier::class -> getSupplier
            ItemIncome::class -> getItemIncome
            ItemOutcome::class -> getItemOutcome
            Project::class -> getProject
            else -> throw IllegalArgumentException("not found update-use-case for entity class: $entityClass")
        }
        return a as GetEntity<ID, out T>
    }
}