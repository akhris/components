package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.ObjectType

typealias GetObjectType = GetEntity<String, ObjectType>
typealias InsertObjectType = InsertEntity<String, ObjectType>
typealias RemoveObjectType = RemoveEntity<String, ObjectType>
typealias UpdateObjectType = UpdateEntity<String, ObjectType>
typealias GetObjectTypes = GetListItemsUseCase<String, ObjectType>
