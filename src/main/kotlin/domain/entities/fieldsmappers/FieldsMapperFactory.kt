package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass


class FieldsMapperFactory {

    fun <T : IEntity<*>> getFieldsMapper(entityClass: KClass<out T>): IFieldsMapper<T> {
        return when (entityClass) {
            Unit::class -> UnitFieldsMapper()
            Parameter::class -> ParameterFieldsMapper()
            ObjectType::class -> ObjectTypeFieldsMapper()
            Item::class -> ItemFieldsMapper()
            Container::class -> ContainerFieldsMapper()
            Supplier::class -> SupplierFieldsMapper()
            ItemIncome::class -> ItemIncomeFieldsMapper()
            ItemOutcome::class -> ItemOutcomeFieldsMapper()
            Project::class -> ProjectFieldsMapper()
            WarehouseItem::class -> WarehouseItemFieldsMapper()
            Invoice::class -> InvoiceFieldsMapper()
            else -> throw IllegalArgumentException("$this cannot get factory for $entityClass")
        } as IFieldsMapper<T>
    }
}

