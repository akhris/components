package domain.application

import com.akhris.domain.core.application.*
import domain.entities.WarehouseItem

typealias    GetWarehouseItem = GetEntity<String, WarehouseItem>
typealias InsertWarehouseItem = InsertEntity<String, WarehouseItem>
typealias RemoveWarehouseItem = RemoveEntity<String, WarehouseItem>
typealias UpdateWarehouseItem = UpdateEntity<String, WarehouseItem>
typealias    GetWarehouseItemsList = GetEntities<String, WarehouseItem>