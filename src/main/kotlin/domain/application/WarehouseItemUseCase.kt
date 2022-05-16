package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.WarehouseItem

typealias    GetWarehouseItem = GetEntity<String, WarehouseItem>
typealias InsertWarehouseItem = InsertEntity<String, WarehouseItem>
typealias RemoveWarehouseItem = RemoveEntity<String, WarehouseItem>
typealias UpdateWarehouseItem = UpdateEntity<String, WarehouseItem>
typealias    GetWarehouseItemsList = GetListItemsUseCase<String, WarehouseItem>