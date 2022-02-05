package domain.repository

import com.akhris.domain.core.repository.IRepository
import domain.entities.Item
import domain.entities.ObjectType

interface IItemsRepository : IRepository<String, Item> {
    suspend fun getItemsByType(typeID: String): List<Item>
}

interface ITypesRepository : IRepository<String, ObjectType> {
    suspend fun getAllItemsTypes(): List<ObjectType>
}