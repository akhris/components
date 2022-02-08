package persistence.repository

import domain.entities.Item
import domain.repository.IItemsRepository

class ItemsTestRepository : BaseEntityTestRepository<String, Item>(), IItemsRepository