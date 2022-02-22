@file:Suppress("UNCHECKED_CAST")

package domain.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass


class RepositoryFactory(
    private val itemsRepository: IItemsRepository,
    private val parametersRepository: IParametersRepository,
    private val unitsRepository: IUnitsRepository,
    private val objectsTypesRepository: ITypesRepository,
    private val containersRepository: IContainersRepository,
    private val suppliersRepository: ISuppliersRepository,
    private val warehouseItemRepository: IWarehouseItemRepository
) {

    fun <ID, T : IEntity<ID>> getRepository(entityClass: KClass<T>): IRepository<ID, T> {
        val a = when (entityClass) {
            Item::class -> itemsRepository
            Parameter::class -> parametersRepository
            Unit::class -> unitsRepository
            ObjectType::class -> objectsTypesRepository
            Container::class -> containersRepository
            Supplier::class -> suppliersRepository
            WarehouseItem::class -> warehouseItemRepository
            else -> throw IllegalArgumentException("not found update-use-case for entity class: $entityClass")
        }
        return a as IRepository<ID, T>
    }
}


