package persistence.columnMappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.*
import domain.entities.Unit
import domain.entities.fieldsmappers.IDBColumnMapper
import kotlin.reflect.KClass

class ColumnMappersFactory {

    fun <T : IEntity<*>> getColumnMapper(entityClass: KClass<out T>): IDBColumnMapper<T> {
        return when (entityClass) {
            Unit::class -> UnitExposedColumnMapper()
            Parameter::class -> ParameterExposedColumnMapper()
            ObjectType::class -> ObjectTypeExposedColumnMapper()
            Item::class -> ItemExposedColumnMapper()
            Container::class -> ContainersExposedColumnMapper()
            Supplier::class -> SupplierExposedColumnMapper()
            ItemIncome::class -> ItemIncomeExposedColumnMapper()
            ItemOutcome::class -> ItemOutcomeExposedColumnMapper()
            Project::class -> ProjectExposedColumnMapper()
            WarehouseItem::class -> WarehouseItemExposedColumnMapper()
            else -> throw IllegalArgumentException("$this cannot get column mapper factory for $entityClass")
        } as IDBColumnMapper<T>
    }
}