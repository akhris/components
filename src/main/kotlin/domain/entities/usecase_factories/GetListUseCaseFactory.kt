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
    private val getContainersList: GetContainersList,
    private val getSuppliersList: GetSuppliersList,
    private val getItemIncomesList: GetItemIncomesList,
    private val getItemOutcomesList: GetItemOutcomesList,
    private val getProjectsList: GetProjectsList,
    private val getWareHouseItemsList: GetWarehouseItemsList
) :
    IGetListUseCaseFactory {

    override fun <T : IEntity<*>> getListUseCase(entityClass: KClass<out T>): GetEntities<*, out T> {
        val a = when (entityClass) {
            Item::class -> getItemsList
            Parameter::class -> getParametersList
            Unit::class -> getUnits
            ObjectType::class -> getObjectTypes
            Container::class -> getContainersList
            Supplier::class -> getSuppliersList
            ItemIncome::class -> getItemIncomesList
            ItemOutcome::class -> getItemOutcomesList
            Project::class -> getProjectsList
            WarehouseItem::class -> getWareHouseItemsList
            else -> throw IllegalArgumentException("not found get-list-use-case for entity class: $entityClass")
        }
        return a as GetEntities<*, out T>
    }

}