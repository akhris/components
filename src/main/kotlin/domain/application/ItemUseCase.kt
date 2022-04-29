package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Item

typealias GetItem = GetEntity<String, Item>
typealias InsertItem = InsertEntity<String, Item>
typealias RemoveItem = RemoveEntity<String, Item>
typealias UpdateItem = UpdateEntity<String, Item>
typealias GetItemsList = GetListItemsUseCase<String, Item>
