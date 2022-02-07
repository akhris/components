package domain.repository

import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.IRepositoryCallback
import domain.entities.Item
import domain.entities.ObjectType

interface IItemsRepository : IRepository<String, Item>, IRepositoryCallback<Item> {
    suspend fun getItemsByType(typeID: String): List<Item>
}

interface ITypesRepository : IRepository<String, ObjectType>, IRepositoryCallback<ObjectType> {
    suspend fun getAllItemsTypes(): List<ObjectType>
}