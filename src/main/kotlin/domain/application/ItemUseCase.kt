package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Item

typealias GetItem = GetEntity<String, Item>
typealias InsertItem = InsertEntity<String, Item>
typealias RemoveItem = RemoveEntity<String, Item>
typealias UpdateItem = UpdateEntity<String, Item>
typealias GetItemsList = GetEntities<String, Item>
